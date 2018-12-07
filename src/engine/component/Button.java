package engine.component;

import org.joml.Vector2f;

import engine.component.graphic.SpriteRenderer;
import engine.component.physic.ShapeType;
import engine.input.InputMouseButton;
import engine.object.Component;
import engine.object.UIObject;

public class Button extends Component {

	@Override
	protected void Update() {
		if (pressedTexture != 0 && IsHeld()) {
			gameObject.GetComponent(SpriteRenderer.class).SetTexture(pressedTexture);
		} else if (hoverTexture != 0 && IsHover()) {
			gameObject.GetComponent(SpriteRenderer.class).SetTexture(hoverTexture);
		} else {
			gameObject.GetComponent(SpriteRenderer.class).SetTexture(unhoverTexture);
		}
	}

	@Override
	protected void Start() {
		gameObject.transform.setPosition(0, 0, 0);
	}

	private ShapeType shapeType = ShapeType.box;
	private Vector2f size = null;

	public Button setShapeType(ShapeType shapeType, Vector2f size) {
		this.shapeType = shapeType;
		this.size = size;
		return this;
	}

	public Button setHoverTexture(int hovertexture) {
		this.hoverTexture = hovertexture;
		return this;
	}

	public Button setPressedTexture(int pressedTexture) {
		this.pressedTexture = pressedTexture;
		return this;
	}

	public Button(int unhoverTexture) {
		this.unhoverTexture = unhoverTexture;
	}

	private float r_limit;
	private float l_limit;
	private float u_limit;
	private float b_limit;

	private int hoverTexture = 0;
	private int unhoverTexture = 0;
	private int pressedTexture = 0;

	public boolean IsHover() {
		// get mouse position
		Vector2f MousePosition;
		if (gameObject instanceof UIObject) {
			MousePosition = Camera.MAIN.ScreenToWorldPositionWITHOUTscrollWITHOUTcamera();
		} else {
			MousePosition = Camera.MAIN.ScreenToWorldPositionWITHOUTSCROLL();
		}
		// check collision
		if (shapeType == ShapeType.box) {
			if (size != null) {
				l_limit = gameObject.transform.position.x + size.x / 2f;
				r_limit = gameObject.transform.position.x - size.x / 2f;
				u_limit = gameObject.transform.position.y + size.y / 2f;
				b_limit = gameObject.transform.position.y - size.y / 2f;
			} else {
				l_limit = gameObject.transform.position.x + gameObject.transform.getScale().x * 50;
				r_limit = gameObject.transform.position.x - gameObject.transform.getScale().x * 50;
				u_limit = gameObject.transform.position.y + gameObject.transform.getScale().y * 50;
				b_limit = gameObject.transform.position.y - gameObject.transform.getScale().y * 50;
			}

			if (MousePosition.x < l_limit && MousePosition.x > r_limit && MousePosition.y < u_limit
					&& MousePosition.y > b_limit) {
				return true;
			}
		}
		if (shapeType == ShapeType.circle) {
			if (MousePosition.distance(
					new Vector2f(gameObject.transform.position.x, gameObject.transform.position.y)) < size.x) {
				return true;
			}
		}

		return false;
	}

	public boolean IsClicked() {
		if (InputMouseButton.OnMouseDown(0)) {
			return IsHover();
		}
		return false;
	}

	public boolean IsHeld() {
		if (InputMouseButton.OnMouseHold(0)) {
			return IsHover();
		}
		return false;
	}
}
