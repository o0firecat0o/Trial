package engine.input;

import org.lwjgl.glfw.*;

public class InputMouseScroll extends GLFWScrollCallback {

	@Override
	public void invoke(long window, double x, double y) {
		// TODO Auto-generated method stub
		if (y == 1) {
			scrollUp = true;
		} else if (y == -1) {
			scrollDown = true;
		}
	}

	public static boolean scrollUp = false;
	public static boolean scrollDown = false;

	public static void Reset() {
		scrollUp = false;
		scrollDown = false;
	}
}
