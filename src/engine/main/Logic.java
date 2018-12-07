package engine.main;

import java.util.concurrent.TimeUnit;

import org.gamestate.MainGameState;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

import engine.component.DebugScreen;
import engine.component.physic.RigidBody;
import engine.component.physic.RigidBodyContactListener;
import engine.gamestate.GameState;
import engine.input.InputKey;
import engine.input.InputMouseButton;
import engine.input.InputMouseScroll;
import engine.input.InputText;
import engine.network.MPNetworkManager;
import engine.network.Packet.Packet2Message;
import engine.object.GameObject;
import engine.object.UpdatableObject;
import engine.timer.Timer;

public class Logic implements Runnable {

	private boolean running = false;

	public static int ups = 0;
	public static int nps = 0;

	// world of Physics
	public static final World world = new World(new Vec2(0, 0), false);

	// debug screen
	public static DebugScreen DEBUG_SCREEN;

	public void init() {
		// set the callback for physics
		world.setContactListener(new RigidBodyContactListener());

		// GameState.SwitchGameState(gameManager);
		GameState.SwitchGameState(new MainGameState());

		// set DebugScreen
		DEBUG_SCREEN = new GameObject().AddComponent(new DebugScreen());
		DEBUG_SCREEN.gameObject.stay_on_stage = true;
	}

	@Override
	public void run() {
		while (Render.finishInit == false) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		init();

		running = true;

		long lastTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		double delta = 0.0;
		int updates = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				// if network is not initiallized
				if (!MPNetworkManager.getConnectionState()) {
					synchronized (Main.lock) {
						update();
						updates++;
						delta--;
					}

				}
				// if network is initiallized
				else {
					// start updating if the network is not locked
					if (!MPNetworkManager.SHOULD_LOCK) {
						synchronized (Main.lock) {
							update();
							updates++;
							delta--;
						}
						// locked the network after this for sync
						MPNetworkManager.SHOULD_LOCK = true;
						MPNetworkManager.SHOULD_SEND = true;

						MPNetworkManager.Frame++;
					} else {
						// tell the server i have finished running the frame;
						if (MPNetworkManager.SHOULD_SEND) {
							Packet2Message message = new Packet2Message();
							message.message = "EndUpdate";

							MPNetworkManager.ClientToServerOnlySend(message);
							// tell once is enough, dont send anymore message afterward
							MPNetworkManager.SHOULD_SEND = false;
						}
					}
				}

				// }
				// if network is initiallized
//				else {
//					// if the network is not locked
//					if (!MPNetworkManager.SHOULD_LOCK) {
//						if (MPNetworkManager.Frame == 0) {
//							// GameState.SwitchGameState(MPNetworkManager.gameStateAfterInitiation);
//						}
//						update();
//						updates++;
//						delta--;
//						// after finishing the update loop, time for sending
//						// for server, it will be sending to client
//						// for client, it will be sending to server
//						MPNetworkManager.SHOULD_SEND = true;
//						// lock the thread and wait for server/client to finish
//						// the loop
//						MPNetworkManager.SHOULD_LOCK = true;
//						// frame counter
//						MPNetworkManager.Frame++;
//					}
//				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				ups = updates;
				updates = 0;
				nps = MPNetworkManager.Frame - nps;
			}
			if (Render.running == false && Render.finishInit == true) {
				running = false;
			}
		}
	}

	public void update() {
		// Check whether the game state needed to be change or not
		GameState.Update();

		// Physics Rendering
		world.step(1 / 60f, 1, 2);
		// normally (1 / 60f, 8, 3)

		// Update all gameObjects
		for (int i = 0; i < UpdatableObject.AllUpdatableObject.size(); i++) {
			UpdatableObject.AllUpdatableObject.get(i).Update();
		}

		// update the current gameState
		if (GameState.currentGameState != null) {
			GameState.currentGameState.Update();
		}

		// run all the timers
		for (int i = 0; i < Timer.timerArrays.size(); i++) {
			Timer.timerArrays.get(i).Update();
		}

		if (!world.isLocked()) {

			// destroy all the bodies
			for (int i = 0; i < RigidBody.tobeDestroyBody.size(); i++) {
				Body vbody = RigidBody.tobeDestroyBody.get(i);

				Fixture fixture = vbody.getFixtureList();
				// loop through all the fixtures
				while (fixture != null) {
					vbody.getFixtureList().setUserData(null);
					Fixture originalFixutre = fixture;
					fixture = fixture.getNext();
					vbody.destroyFixture(originalFixutre);
				}
				world.destroyBody(vbody);
			}
			RigidBody.tobeDestroyBody.clear();
		}

		// destroy all the gameobject
		UpdatableObject.DestroyAllDestroyNextFrameObjects();

		// reset Mouse and Keyboard
		InputMouseButton.Reset();
		InputMouseScroll.Reset();
		InputKey.Reset();
		InputText.Reset();

	}
}
