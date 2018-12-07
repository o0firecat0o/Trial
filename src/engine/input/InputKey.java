package engine.input;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class InputKey extends GLFWKeyCallback {

	private static boolean[] keysHold = new boolean[5000];

	private static boolean[] keysDown = new boolean[5000];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (key == -1) {
			return;
		}
		keysHold[key] = action != GLFW.GLFW_RELEASE;
		if (action == GLFW.GLFW_PRESS) {
			keysDown[key] = true;
		}
	}

	public static boolean OnKeysDown(int Key) {
		return keysDown[Key];
	}

	public static boolean OnKeysHold(int Key) {
		return keysHold[Key];
	}

	public static void Reset() {
		for (int i = 0; i < keysDown.length; i++) {
			keysDown[i] = false;
		}
	}

	private static ArrayList<Integer> returnArrayListforKeysDown = new ArrayList<>();

	public static ArrayList<Integer> KeysDown() {
		returnArrayListforKeysDown.clear();
		for (int i = 0; i < keysDown.length; i++) {
			if (keysDown[i] == true) {
				returnArrayListforKeysDown.add(i);
			}
		}
		return returnArrayListforKeysDown;
	}
}
