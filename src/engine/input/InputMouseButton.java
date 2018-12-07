package engine.input;

import org.lwjgl.glfw.*;

public class InputMouseButton extends GLFWMouseButtonCallback {

	private static boolean[] MousekeysHold = new boolean[50];
	private static boolean[] MousekeysDown = new boolean[50];
	private static boolean[] MousekeysUp = new boolean[50];

	@Override
	public void invoke(long window, int mouseKey, int mouseAction, int modifierKeys) {
		MousekeysHold[mouseKey] = mouseAction != GLFW.GLFW_RELEASE;
		if (mouseAction == GLFW.GLFW_PRESS) {
			MousekeysDown[mouseKey] = true;
		}
		if (mouseAction == GLFW.GLFW_RELEASE) {
			MousekeysUp[mouseKey] = true;
		}
	}

	public static boolean OnMouseHold(int MouseKey) {
		return MousekeysHold[MouseKey];
	}

	public static boolean OnMouseDown(int MouseKey) {
		return MousekeysDown[MouseKey];
	}

	public static boolean OnMouseUp(int MouseKey) {
		return MousekeysUp[MouseKey];
	}

	public static void Reset() {
		for (int i = 0; i < MousekeysDown.length; i++) {
			MousekeysDown[i] = false;
		}

		for (int i = 0; i < MousekeysUp.length; i++) {
			MousekeysUp[i] = false;
		}
	}
}
