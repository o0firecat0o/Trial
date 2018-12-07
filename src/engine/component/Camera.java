package engine.component;

import org.jbox2d.common.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.component.graphic.Shader;
import engine.component.graphic.SpriteRenderer;
import engine.input.InputKey;
import engine.input.InputMousePos;
import engine.input.InputMouseScroll;
import engine.main.Main;
import engine.math.Mathf;
import engine.object.Component;

public class Camera extends Component {

	/**
	 * Mouse scroll that resize the screen
	 */
	public float scroll = 1;

	public static Camera MAIN;

	public boolean enable = true;

	@Override
	public void UpdateRender() {
		// Rendering
		for (int i = 0; i < Shader.returnAllShaders().length; i++) {
			Shader shader = (Shader) Shader.returnAllShaders()[i];
			if (Shader.rendered_shader.contains(shader)) {
				// if it is an UI shader, do not apply scale
				if (shader.isUIShader) {
					shader.setUniformMat4f("vw_matrix", new Matrix4f().translate(0, 0, 0));

					shader.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix);
					continue;
				}

				// else apply scale and translation
				shader.setUniformMat4f("vw_matrix",
						new Matrix4f().translate(-gameObject.transform.position.x, -gameObject.transform.position.y, 0)
								.scale(scroll));

				shader.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix);
			}
		}
		// Clear the rendered shader list
		Shader.rendered_shader.clear();
	}

	@Override
	protected void Update() {
		if (!enable) {
			return;
		}

		// Moving
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_UP)) {
			gameObject.transform.position.y += 10f;
		}
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_DOWN)) {
			gameObject.transform.position.y -= 10f;
		}
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_RIGHT)) {
			gameObject.transform.position.x += 10f;
		}
		if (InputKey.OnKeysHold(GLFW.GLFW_KEY_LEFT)) {
			gameObject.transform.position.x -= 10f;
		}

		// Srolling
		if (InputMouseScroll.scrollUp) {
			scroll *= 1.1;
		} else if (InputMouseScroll.scrollDown) {
			scroll *= 0.9;
		}
		scroll = Mathf.clamp(scroll, 0.02f, 80f);
	}

	@Override
	protected void Start() {
		gameObject.transform.setPosition(0, 0, 0);
	}

	public Vector2f InputMousePositionV2f() {
		Vector2f screenVector = InputMousePos.RelativePos;
		Vector2f worldVector = new Vector2f();
		worldVector.x = screenVector.x * 1280 / Main.getWidth();
		worldVector.y = screenVector.y * 720 / Main.getHeight();
		worldVector.x += gameObject.transform.position.x / scroll;
		worldVector.y += gameObject.transform.position.y / scroll;
		return worldVector;
	}

	public Vec2 InputMousePositionVec2() {
		Vector2f screenVector = InputMousePos.RelativePos;
		Vec2 worldVector = new Vec2();
		worldVector.x = screenVector.x * 1280 / Main.getWidth();
		worldVector.y = screenVector.y * 720 / Main.getHeight();
		worldVector.x += gameObject.transform.position.x / scroll;
		worldVector.y += gameObject.transform.position.y / scroll;
		return worldVector;
	}

	public Vector3f InputMousePositionV3f() {
		return new Vector3f(InputMousePositionV2f().x, InputMousePositionV2f().y, 0);
	}

	public Vector2f ScreenToWorldPositionWITHOUTSCROLL() {
		Vector2f screenVector = InputMousePos.ScreenPos;
		Vector2f worldVector = new Vector2f();
		worldVector.x = screenVector.x * 1280 / Main.getWidth();
		worldVector.y = screenVector.y * 720 / Main.getHeight();
		worldVector.x += gameObject.transform.position.x;
		worldVector.y += gameObject.transform.position.y;
		return worldVector;
	}

	public Vector2f ScreenToWorldPositionWITHOUTscrollWITHOUTcamera() {
		Vector2f screenVector = InputMousePos.ScreenPos;
		Vector2f worldVector = new Vector2f();
		worldVector.x = screenVector.x * 1280 / Main.getWidth();
		worldVector.y = screenVector.y * 720 / Main.getHeight();
		return worldVector;
	}

	public Vector2f WorldToScreenPosition(Vector2f original) {
		Vector2f returnVector = new Vector2f();
		returnVector.add(original);
		returnVector.x = returnVector.x / Main.getWidth();
		returnVector.y = returnVector.y / Main.getHeight();
		return returnVector;
	}

	// TODO: fix this shit (when resizing the monitor it failed)
	public float WorldToScreenPositionX(float x) {
		float ScreenX = 0;
		ScreenX += x * scroll;
		ScreenX -= gameObject.transform.position.x;
		ScreenX = ScreenX / Main.getWidth();
		return ScreenX;
	}

	// TODO: fix this shit (when resizing the monitor it failed)
	public float WorldToScreenPositionY(float y) {
		float ScreenY = 0;
		ScreenY += y * scroll;
		ScreenY -= gameObject.transform.position.y;
		ScreenY = ScreenY / Main.getHeight();
		return ScreenY;
	}
}
