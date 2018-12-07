package engine.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.gamestate.MainGameState;

import engine.gamestate.GameState;
import engine.network.Packet.Packet2Message;
import engine.network.Packet.Packet4Function;
import engine.network.jPanel.IJPanelNetworkIPInputField;
import engine.network.jPanel.IJPanelNetworkSelectServerClient;
import engine.network.jPanel.JPanelNetworkIPInputField;
import engine.network.jPanel.JPanelNetworkSelectServerClient;

/**
 * 
 * The class for multiplayer networking instantiation! prevent using MPClient or
 * MPServer Independently.
 * 
 * @author Ringo
 *
 */
public class MPNetworkManager {

	public static MPServer server;
	public static MPClient client;

	public static ArrayList<ConnectionProperty> allHostsIP = new ArrayList<>();

	private static boolean connectionState = false;

	/**
	 * Has the NetworkServer been initialized?
	 * 
	 * @return
	 */
	public static boolean getConnectionState() {
		return connectionState;
	}

	public static int ConnectionID = -1;

	// originally the should lock should be true
	// locking both server and client waiting for initiation
	public static boolean SHOULD_LOCK = false;
	public static boolean SHOULD_SEND = false;

	// Frame Counter
	public static int Frame = 0;

	public static Map<Integer, INetworkFunction> ID_Function_Dictionary = new HashMap<>();

	public static void ClientToServerOnlySend(Object object) {
		if (client != null) {
			client.client.sendTCP(object);
		}
	}

	public static void ServerToClientOnlySend(Object object) {
		if (server != null) {
			server.server.sendToAllTCP(object);
		}
	}

	public static void Send(Object object) {
		if (server != null) {
			server.server.sendToAllTCP(object);
		}
		if (client != null) {
			client.client.sendTCP(object);
		}
	}

	public static boolean InitServer() {
		try {
			server = new MPServer();
			System.out.println("serveropened!");
			ConnectionID = 0;

			// make the network random from a seed
			long t = System.currentTimeMillis();
			server.randomSeed = t;
			engine.random.Random.SyncRandom(t);

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean InitClient(String ip) {
		try {
			client = new MPClient(ip);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	// switch to the gamescene
	// start pausing the thread
	public static void StartGame() {
		Packet2Message message = new Packet2Message();
		message.message = "StartGame";
		// tell every client that we are now going to start the game
		ServerToClientOnlySend(message);

		System.out.println("[Network] The game now starts with " + allHostsIP.size() + " Player");
		// Change to the gamestate you wanna switch here
		GameState.SwitchGameState(new MainGameState());

		connectionState = true;
	}

	public static void InitServerClientByPanel() {
		new JPanelNetworkSelectServerClient().hookedInterface = new IJPanelNetworkSelectServerClient() {
			@Override
			public void run(boolean isServer) {
				if (isServer) {
					InitServer();
				} else {
					InitClientByPanel();
				}
			}
		};
	}

	/*
	 * get the ip address by jPanel
	 */
	private static void InitClientByPanel() {
		new JPanelNetworkIPInputField().hookedInterface = new IJPanelNetworkIPInputField() {

			@Override
			public void run(String ip) {
				InitClient(ip);
			}
		};
	}

	private static int FunctionIDCount = 0;

	private static int genNewFunctionID() {
		FunctionIDCount++;
		return FunctionIDCount;
	}

	public static int RegisterFunction(INetworkFunction function) {
		int ID = genNewFunctionID();
		ID_Function_Dictionary.put(ID, function);
		return ID;
	}

	public static void UnRegisterFunction(int functionID) {
		ID_Function_Dictionary.remove(ID_Function_Dictionary.get(functionID));
	}

	public static ArrayList<Packet4Function> functionList = new ArrayList<Packet4Function>();

	public static void sendFunction(int FunctionID, Object[] objects) {
		// if I am a client
		if (client != null) {
			Packet4Function functionPackage = new Packet4Function();
			functionPackage.functionID = FunctionID;
			functionPackage.objects = objects;
			ClientToServerOnlySend(functionPackage);
		}
		// if I am a server
		if (server != null) {
			// no need to send to client now, just save it
			// it will be sent to client later
			Packet4Function functionPackage = new Packet4Function();
			functionPackage.functionID = FunctionID;
			functionPackage.objects = objects;
			functionList.add(functionPackage);
		}
		// if i am neither any, singleplayer
		if (client == null && server == null) {
			ID_Function_Dictionary.get(FunctionID).run(objects);
		}
	}
}
