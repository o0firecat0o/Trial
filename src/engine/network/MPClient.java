package engine.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import engine.main.Main;
import engine.network.Packet.Packet1allHostsIP_ID;
import engine.network.Packet.Packet2Message;
import engine.network.Packet.Packet4Function;
import engine.network.Packet.Packet5RandomSeed;
import engine.random.Random;

public class MPClient extends Listener {
	public Client client;

	public MPClient(String IPAdress) throws IOException {
		client = new Client();
		Packet.RegisterAll(client.getKryo());

		client.addListener(this);

		client.start();

		client.connect(5000, IPAdress, 55545);

	}

	@Override
	public void connected(Connection arg) {
		System.out.println("[CLIENT]" + "Client has connected to: " + arg.getRemoteAddressTCP());
		MPNetworkManager.ConnectionID = arg.getID();
	}

	@Override
	public void disconnected(Connection arg) {
		System.out.println("[CLIENT]" + "Client has disconnected fraom: " + arg.getRemoteAddressTCP());
	}

	@Override
	public void received(Connection connection, Object object) {

		if (object instanceof Packet1allHostsIP_ID) {
			Integer[] IDs = ((Packet1allHostsIP_ID) object).IDs;
			String[] IPs = ((Packet1allHostsIP_ID) object).IPs;

			MPNetworkManager.allHostsIP.clear();
			for (int i = 0; i < IPs.length; i++) {
				MPNetworkManager.allHostsIP.add(new ConnectionProperty(IDs[i], IPs[i]));
			}
		}

		if (object instanceof Packet2Message) {
			String message = ((Packet2Message) object).message;
			// if the server send a message to the client that the client should
			// now start running
			if (message.equals("EndOfAllFunction")) {
				MPNetworkManager.SHOULD_LOCK = false;
			}
			// if the server send a message to the client that the game should now starts
			if (message.equals("StartGame")) {
				MPNetworkManager.StartGame();
			}
		}

		if (object instanceof Packet4Function) {
			synchronized (Main.lock) {
				int FunctionID = ((Packet4Function) object).functionID;
				Object[] objects = ((Packet4Function) object).objects;
				MPNetworkManager.ID_Function_Dictionary.get(FunctionID).run(objects);
			}
		}

		if (object instanceof Packet5RandomSeed) {
			Packet5RandomSeed message = ((Packet5RandomSeed) object);
			Random.SyncRandom(message.seed);
		}
	}
}
