package org.gamestate;

import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.Texture;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.main.Render;
import engine.object.Component;
import engine.random.Random;

public class ExplosionA extends Component {

	@Override
	protected void Update() {
		gameObject.transform.setScale(gameObject.transform.getScale().mul(0.95f));
		gameObject.GetComponent(SpriteRenderer.class).getSpriteRendererComponent(DefaultRender.class).alpha -= 0.01f;
		if (gameObject.transform.getScale().x <= 0) {
			gameObject.InitDestroy();
		}
	}

	@Override
	protected void Start() {
		gameObject.AddComponent(new SpriteRenderer())
				.addSpriteRendererComponent(new DefaultRender().SetFrameBuffer(Render.mainFrameBuffer));
		gameObject.GetComponent(SpriteRenderer.class)
				.SetTexture(Texture.getTexture("Explosion" + Random.RandomInt(1, 2)));
		gameObject.transform.rotation = Random.RandomFloat(0f, (float) Math.PI * 2);
		gameObject.transform.setScale(Random.RandomFloat(2, 4));
	}

	public ExplosionA() {
		super();
	}

}
