package engine.input;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWCharCallback;

public class InputText extends GLFWCharCallback {

	@Override
	public void invoke(long arg0, int arg1) {
		returnArrayListforKeysDown.add(arg1);

	}

	public static void Reset() {
		returnArrayListforKeysDown.clear();
	}

	private static ArrayList<Integer> returnArrayListforKeysDown = new ArrayList<>();

	public static ArrayList<Integer> KeysDown() {
		return returnArrayListforKeysDown;
	}

}
