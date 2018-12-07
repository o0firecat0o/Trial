package org.Stage;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.Texture;
import engine.component.graphic.effects.Effect;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.component.physic.EntityCategory;
import engine.component.physic.RigidBody;
import engine.component.physic.RigidFixutre;
import engine.component.physic.ShapeType;
import engine.math.Mathf;
import engine.object.Component;

public class Bullet extends Component {

	@Override
	protected void Update() {
		gameObject.GetComponent(RigidBody.class)
				.setVelocity(Vector2fClamp(gameObject.GetComponent(RigidBody.class).getVelocity(), 25));
		timer++;
		// after getting into the play circle, start adding collision
		if (timer > 70) {
			gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.ROCK);
			gameObject.GetComponent(RigidBody.class).addMaskBits(EntityCategory.TEAM2);
		}
	}

	int Timer = 0;
	final Vector3f Color;

	public Bullet(Vector3f Color) {
		super();
		this.Color = Color;
	}

	@Override
	protected void Start() {
		// TODO Auto-generated method stub
		gameObject.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new DefaultRender());
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("BulletA"));
		gameObject.AddComponent(new RigidBody());
		gameObject.AddComponent(new RigidFixutre(ShapeType.box, new Vector2f(1f, 1f),
				gameObject.GetComponent(RigidBody.class), new Vector2f(), 0));
		gameObject.GetComponent(RigidFixutre.class).setRestitution(0.1f);
		gameObject.GetComponent(RigidFixutre.class).setFriction(0);
		gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.TEAM1);
		gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.NULL);
		gameObject.GetComponent(RigidBody.class)
				.AddForce(Mathf.Vector2ftoVec2(Mathf.Vector2fFromAngle(gameObject.transform.rotation)));
		gameObject.GetComponent(RigidBody.class).setAngularDrag(0);
		gameObject.GetComponent(RigidBody.class).setLinearDrag(0);
		gameObject.GetComponent(RigidBody.class).setBodyType(BodyType.DYNAMIC);
		Effect.Tail(gameObject.GetComponent(SpriteRenderer.class), Texture.getTexture("BlurDot"), 0,
				new Vector4f(Color, 1f));
	}

	public Vec2 Vector2fClamp(Vec2 v, float max) {
		Vec2 q;

		q = new Vec2();
		float ratio = max / v.length();
		q.x = v.x * ratio;
		q.y = v.y * ratio;
		return q;
	}

	int timer;

	@Override
	public void Destroy() {
		super.Destroy();
	}

}
