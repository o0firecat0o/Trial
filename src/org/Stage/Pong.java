package org.Stage;

import java.util.ArrayList;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.component.Camera;
import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.Texture;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.component.physic.EntityCategory;
import engine.component.physic.IContact;
import engine.component.physic.Physics;
import engine.component.physic.RigidBody;
import engine.component.physic.RigidFixutre;
import engine.component.physic.ShapeType;
import engine.math.Mathf;
import engine.object.Component;
import engine.object.GameObject;

public class Pong extends Component implements IContact, QueryCallback {

	@Override
	protected void Update() {
		// Graphic effect

		inP.transform.setPosition(gameObject.transform.getPositionVector2f());
		if (OutR != null) {
			OutR.alpha -= 0.04f;
			OutR.alpha = Mathf.clamp(OutR.alpha, 0, 0.4f);
		}
		// physics
		remakePong();
		// actual moving of the pong
		float x;
		float y;
		if (!AIControlled) {
			x = Interpolation(gameObject.transform.position.x, Camera.MAIN.InputMousePositionV2f().x, speed);
			y = 0;
		} else {
			ArrayList<Fixture> fixtures = Physics.OverlapAABB(new Vector2f(-4500, -4500), new Vector2f(4500, 4500));
			float disatnce = 10000000f;
			Vector2f vector2f = new Vector2f();

			for (int i = 0; i < fixtures.size(); i++) {
				if (fixtures.get(i).m_filter.categoryBits == EntityCategory.TEAM1.getValue()) {
					if (location == 0) {
						float dy = fixtures.get(i).m_body.getPosition().y * 100 - -4500;
						dy = Math.abs(dy);
						if (dy < disatnce && fixtures.get(i).m_body.getPosition().y * 100 > -4500) {
							disatnce = dy;
							vector2f.x = fixtures.get(i).m_body.getPosition().x * 100;
						}
					} else if (location == 1) {
						float dx = fixtures.get(i).m_body.getPosition().x * 100 - -4500;
						dx = Math.abs(dx);
						if (dx < disatnce && fixtures.get(i).m_body.getPosition().x * 100 > -4500) {
							disatnce = dx;
							vector2f.y = fixtures.get(i).m_body.getPosition().y * 100;
						}
					} else if (location == 2) {
						float dy = fixtures.get(i).m_body.getPosition().y * 100 - 4500;
						dy = Math.abs(dy);
						if (dy < disatnce && fixtures.get(i).m_body.getPosition().y * 100 < 4500) {
							disatnce = dy;
							vector2f.x = fixtures.get(i).m_body.getPosition().x * 100;
						}
					} else if (location == 3) {
						float dx = fixtures.get(i).m_body.getPosition().x * 100 - 4500;
						dx = Math.abs(dx);
						if (dx < disatnce && fixtures.get(i).m_body.getPosition().x * 100 < 4500) {
							disatnce = dx;
							vector2f.y = fixtures.get(i).m_body.getPosition().y * 100;
						}
					}

				}
				;
			}
			if (disatnce == 10000000f) {
				x = gameObject.transform.position.x;
				y = gameObject.transform.position.y;
			} else {
				x = vector2f.x;
				y = vector2f.y;
			}
		}
		float finx = Interpolation(gameObject.transform.position.x, x, speed);
		float finy = Interpolation(gameObject.transform.position.y, y, speed);
		if (location == 0 || location == 2) {
			gameObject.transform.setPosition(finx, gameObject.transform.getPositionVector2f().y);
		} else {
			gameObject.transform.setPosition(gameObject.transform.getPositionVector2f().x, finy);
		}

	}

	void remakePong() {
		// Physics
		for (int i = 0; i < PongList.size(); i++) {
			if (PongList.get(i).HasComponent(RigidFixutre.class)) {
				PongList.get(i).GetComponent(RigidFixutre.class).UnRegisterContactCallBack(this);
			}
			PongList.get(i).InitDestroy();
		}

		GameObject gameObject = new GameObject();
		gameObject.transform.setPosition(this.gameObject.transform.position);
		gameObject.transform.setRotation(this.gameObject.transform.rotation);
		gameObject.AddComponent(new RigidBody());
		gameObject.AddComponent(new RigidFixutre(ShapeType.box, new Vector2f(10, 2f),
				gameObject.GetComponent(RigidBody.class), new Vector2f(), 0));
		gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.TEAM2);
		gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.TEAM1);
		gameObject.GetComponent(RigidBody.class).setPhysicsDetermineTransform(false);
		gameObject.GetComponent(RigidFixutre.class).RegisterContactCallBack(this);
		gameObject.GetComponent(RigidFixutre.class).setFriction(0);
		gameObject.GetComponent(RigidFixutre.class).setRestitution(0.9f);
		PongList.add(gameObject);
	}

	GameObject inP;
	DefaultRender OutR;
	ArrayList<GameObject> PongList = new ArrayList<>();
	public final Vector3f Color;

	public final float location;
	// 0 bot
	// 1 left
	// 2 top
	// 3 up

	public boolean AIControlled = true;

	public Pong(float location, Vector3f Color, boolean AIControlled) {
		super();
		this.location = location;
		this.Color = Color;
		this.AIControlled = AIControlled;
	}

	@Override
	protected void Start() {
		// set location
		if (location == 0) {
			gameObject.transform.setPosition(new Vector2f(0, -4500));
		} else if (location == 1) {
			gameObject.transform.setPosition(new Vector2f(-4500, 0));
			gameObject.transform.setRotation((float) (Math.PI / 2f));
		} else if (location == 2) {
			gameObject.transform.setPosition(new Vector2f(0, 4500));
		} else if (location == 3) {
			gameObject.transform.setPosition(new Vector2f(4500, 0));
			gameObject.transform.setRotation((float) (Math.PI / 2f));
		}

		// Graphics
		OutR = gameObject.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new DefaultRender());
		gameObject.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("OutP"));
		gameObject.transform.setScale(new Vector2f(24, 7));
		OutR.Color.set(Color);
		OutR.alpha = 1;

		inP = new GameObject();
		DefaultRender render = inP.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new DefaultRender());
		render.Color.set(Color);
		render.alpha = 0.4f;
		inP.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("InP"));
		inP.transform.setScale(new Vector2f(24, 7));
		inP.transform.setRotation(gameObject.transform.getRotation());

		remakePong();
	}

	@Override
	public void Contact(org.jbox2d.dynamics.contacts.Contact contact, Fixture target) {
//		if (target.m_body.getPosition().y * RigidBody.RATIO > gameObject.transform.position.y) {
//			target.m_body.applyLinearImpulse(new Vec2(0, 30), target.m_body.getWorldCenter());
//		}

		OutR.alpha += 0.4f;
	}

	@Override
	public void Destroy() {
		for (int i = 0; i < PongList.size(); i++) {
			PongList.get(i).GetComponent(RigidFixutre.class).UnRegisterContactCallBack(this);
			PongList.get(i).InitDestroy();
		}
		inP.InitDestroy();
		super.Destroy();
	}

	private float speed = 0.1f;

	public float Interpolation(float original, float target, float speed) {
		return original + speed * (target - original);
	}

	@Override
	public boolean reportFixture(Fixture fixture) {
		System.out.println(fixture);
		return true;
	}

}
