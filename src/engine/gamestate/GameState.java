package engine.gamestate;

import engine.object.GameObject;
import engine.object.UpdatableObject;
import engine.timer.Timer;

public class GameState {
	public static IGameState currentGameState;
	private static IGameState tobeswitchedgameState;

	public static void SwitchGameState(IGameState gameState) {
		tobeswitchedgameState = gameState;
	}

	public static void Update() {
		if (tobeswitchedgameState == null) {
			return;
		}

		if (currentGameState != null) {
			currentGameState.Stop();
			System.out.println("Switching GameState from: " + currentGameState.getClass() + "to "
					+ tobeswitchedgameState.getClass());
		}
		for (UpdatableObject gameObject : GameObject.AllUpdatableObject) {
			if (!gameObject.stay_on_stage) {
				gameObject.InitDestroy();
			}
		}
		// remove all timers;
		Timer.timerArrays.forEach(x -> x.Destroy());

		System.out.println("Remaing GameObjects: " + GameObject.AllUpdatableObject.size());
		GameState.currentGameState = tobeswitchedgameState;
		tobeswitchedgameState.Init();

		tobeswitchedgameState = null;
	}
}
