package engine.network;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import engine.main.Main;
import engine.network.Packet.Packet1allHostsIP_ID;
import engine.network.Packet.Packet2Message;
import engine.network.Packet.Packet4Function;
import engine.network.Packet.Packet5RandomSeed;

public class MPServer extends Listener {
	Server server;

	public long randomSeed;

	public int connectionCount = 0;

	public MPServer() throws IOException {
		server = new Server();
		Packet.RegisterAll(server.getKryo());
		server.addListener(this);
		server.bind(55545);
		server.start();
		MPNetworkManager.allHostsIP.add(new ConnectionProperty(0, "HOST"));
	}

	@Override
	public void connected(Connection arg) {
		System.out.println("[SERVER] " + arg.getRemoteAddressTCP().getHostString() + " has connected.");
		MPNetworkManager.allHostsIP.add(new ConnectionProperty(arg.getID(), arg.getRemoteAddressTCP().getHostString()));
		connectionCount++;
		// send the ip and id information to the client
		sendConnectedInformationToAllClient();
		// send the random seed to the client
		Packet5RandomSeed seed = new Packet5RandomSeed();
		seed.seed = randomSeed;
		server.sendToTCP(arg.getID(), seed);
	}

	@Override
	public void disconnected(Connection arg) {
		System.out.println("[SERVER] Someone has disconected.");
		for (int i = 0; i < MPNetworkManager.allHostsIP.size(); i++) {
			if (MPNetworkManager.allHostsIP.get(i).connectionID == arg.getID()) {
				MPNetworkManager.allHostsIP.remove(i);
			}
		}
		connectionCount--;

		sendConnectedInformationToAllClient();
	}

	public void sendConnectedInformationToAllClient() {
		Packet1allHostsIP_ID message = new Packet1allHostsIP_ID();
		message.IDs = new Integer[MPNetworkManager.allHostsIP.size()];
		message.IPs = new String[MPNetworkManager.allHostsIP.size()];
		for (int i = 0; i < MPNetworkManager.allHostsIP.size(); i++) {
			message.IDs[i] = MPNetworkManager.allHostsIP.get(i).connectionID;
			message.IPs[i] = MPNetworkManager.allHostsIP.get(i).IP;
		}
		MPNetworkManager.Send(message);
	}

	private int recievedconnection = 0;

	@Override
	public void received(Connection connection, Object object) {

		if (object instanceof Packet2Message) {
			String message = ((Packet2Message) object).message;
			// one of the client has finished running
			if (message.equals("EndUpdate")) {
				recievedconnection++;
				// This is the end of turn

				// if ALL the client has finished running
				if (recievedconnection == connectionCount) {
					synchronized (Main.lock) {
						// run all functions on the server
						MPNetworkManager.functionList
								.forEach(x -> MPNetworkManager.ID_Function_Dictionary.get(x.functionID).run(x.objects));

						// send all functions to the client to run
						MPNetworkManager.functionList.forEach(x -> MPNetworkManager.ServerToClientOnlySend(x));

						// clear the function array and use it for next round
						MPNetworkManager.functionList.clear();
					}

					// wait for some second
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// send to the client that it is the end of all function, a new frame could be
					// started
					Packet2Message message2 = new Packet2Message();
					message2.message = "EndOfAllFunction";

					server.sendToAllTCP(message2);

					recievedconnection = 0;

					// *************************
					// This is the start of new turn
					MPNetworkManager.SHOULD_LOCK = false;
				}
			}
		}

		if (object instanceof Packet4Function) {
			MPNetworkManager.functionList.add((Packet4Function) object);
		}
	}
}
