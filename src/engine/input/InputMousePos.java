package engine.input;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import engine.component.Camera;
import engine.main.Main;
import engine.object.*;

public class InputMousePos extends GLFWCursorPosCallback {

	/**
	 * Screen Position from 0 to 1280, 0 to 720
	 */
	public static Vector2f ScreenPos = new Vector2f();

	/**
	 * World Position after considering scroll
	 */
	public static Vector2f RelativePos = new Vector2f();

	@Override
	public void invoke(long window, double x, double y) {
		ScreenPos.x = (float) x;
		ScreenPos.y = Main.getHeight() - (float) y;
		RelativePos = new Vector2f(ScreenPos.x / Camera.MAIN.scroll, ScreenPos.y / Camera.MAIN.scroll);
	}

	public static boolean overUIObject() {
		for (int i = 0; i < GameObject.AllUpdatableObject.size(); i++) {
			// check if the object is a gameobject or not
			if (GameObject.AllUpdatableObject.get(i) instanceof GameObject) {
				GameObject gObject = (GameObject) GameObject.AllUpdatableObject.get(i);
				// check if the object is blocking raycast or not
				if (gObject.blockRayCast) {
					Vector2f MousePosition;
					if (gObject instanceof UIObject) {
						MousePosition = Camera.MAIN.ScreenToWorldPositionWITHOUTscrollWITHOUTcamera();
					} else {
						MousePosition = Camera.MAIN.ScreenToWorldPositionWITHOUTSCROLL();
					}
					float r_limit = gObject.transform.position.x + gObject.transform.getScale().x * 50;
					float l_limit = gObject.transform.position.x - gObject.transform.getScale().x * 50;
					float u_limit = gObject.transform.position.y + gObject.transform.getScale().y * 50;
					float b_limit = gObject.transform.position.y - gObject.transform.getScale().y * 50;
					// check if inside the boundary
					if (MousePosition.x > l_limit && MousePosition.x < r_limit && MousePosition.y < u_limit
							&& MousePosition.y > b_limit) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
