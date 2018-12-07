package org.Stage;

import org.joml.Vector3f;

import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.Texture;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.main.Render;
import engine.math.Mathf;
import engine.object.Component;
import engine.object.GameObject;

public class Paddle extends Component {

	@Override
	protected void Update() {
		gameObject.transform.setRotation(gameObject.transform.getRotation() + 0.003f);

		alpha -= 0.04f;
		alpha = Mathf.clamp(alpha, 0.2f, 1f);
		gameObject.GetComponent(SpriteRenderer.class).getSpriteRendererComponent(DefaultRender.class).alpha = alpha;
		modifiedAlphaThisFrame = false;

	}

	@Override
	protected void Start() {
		gameObject.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new DefaultRender())
				.AddFrameBuffer(Render.bloomFrameBuffer);
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("Paddle"));
		gameObject.GetComponent(SpriteRenderer.class).getSpriteRendererComponent(DefaultRender.class).Color.set(color);
		gameObject.GetComponent(SpriteRenderer.class).getSpriteRendererComponent(DefaultRender.class).alpha = 0.2f;
		gameObject.transform.setScale(152f);
		gameObject.transform.setRotation(phaseOffset);
	}

	public Paddle(float phaseOffset, Vector3f color) {
		super();
		this.phaseOffset = phaseOffset;
		this.color = color;
	}

	float alpha = 0.2f;
	boolean modifiedAlphaThisFrame = false;
	final float phaseOffset;
	final Vector3f color;

	public void PlayNotes(float notePosition) {
		GameObject noteObject = new GameObject();
		float f = gameObject.transform.rotation + (notePosition - 0.5f) / 3f;
		noteObject.transform.setPosition(Mathf.Vector2fFromAngle((float) (f + Math.PI / 2)).mul(7000));
		noteObject.transform.setRotation((float) (gameObject.transform.rotation - Math.PI / 2));
		noteObject.AddComponent(new Bullet(color));
		if (modifiedAlphaThisFrame == false) {
			alpha += 0.8f;
			modifiedAlphaThisFrame = true;
		}

		GameObject paddleKeyObject = new GameObject();
		paddleKeyObject.transform.setRotation(f);
		paddleKeyObject.transform.setScale(152f);
		paddleKeyObject.AddComponent(new PaddleKey());
	}

}
