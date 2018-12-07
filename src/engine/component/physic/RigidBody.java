package engine.component.physic;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.joml.Vector2f;

import engine.main.Logic;
import engine.object.Component;

public class RigidBody extends Component {

	public static final ArrayList<Body> tobeDestroyBody = new ArrayList<>();

	public static int RATIO = 100;

	private boolean physicsDetermineTransform = true;

	@Override
	protected void Update() {
		// using physic object to move transform around
		if (physicsDetermineTransform) {
			gameObject.transform.position.x = body.getPosition().x * RATIO;
			gameObject.transform.position.y = body.getPosition().y * RATIO;
			gameObject.transform.rotation = body.getAngle();
		}
		// using transform to move physic object around
		else {
			body.setTransform(
					new Vec2(gameObject.transform.position.x / RATIO, gameObject.transform.position.y / RATIO),
					gameObject.transform.rotation);
		}
	}

	public Body body;

	/**
	 * My mask
	 */
	int CategoryBits = 0x0001;
	/**
	 * The mask which I will collide with
	 */
	int MaskBits = 0xFFFF;

	@Override
	protected void Start() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(gameObject.transform.position.x / RATIO, gameObject.transform.position.y / RATIO);
		bodyDef.angle = gameObject.transform.rotation;
		bodyDef.type = BodyType.DYNAMIC;

		body = Logic.world.createBody(bodyDef);
	}

	@Override
	protected void Destroy() {
		tobeDestroyBody.add(body);
		body = null;
		super.Destroy();
	}

	public RigidBody setRotation(float rotation) {
		body.m_sweep.a = rotation;
		return this;
	}

	/**
	 * Set the body type to either dynamic, kinetic, static
	 * 
	 * @param bodyType dynamic, kinetic, static
	 */
	public RigidBody setBodyType(BodyType bodyType) {
		body.setType(bodyType);
		return this;
	}

	public RigidBody setLinearDrag(float value) {
		body.setLinearDamping(value);
		return this;
	}

	public float getLinearDrag() {
		return body.m_linearDamping;
	}

	public RigidBody setAngularDrag(float value) {
		body.setAngularDamping(value);
		return this;
	}

	public float getAngularDrag() {
		return body.m_angularDamping;
	}

	public Vec2 getVelocity() {
		return body.getLinearVelocity().clone();
	}

	/**
	 * A sensor shape collects contact information but never generates a collision
	 * response.
	 */
	public RigidBody setTrigger(boolean isTrigger) {
		Fixture fixture = body.getFixtureList();
		while (fixture != null) {
			fixture.setSensor(isTrigger);
			fixture = fixture.getNext();
		}
		return this;
	}

	public RigidBody setPhysicsDetermineTransform(boolean physicsDetermineTransform) {
		this.physicsDetermineTransform = physicsDetermineTransform;
		return this;
	}

	/**
	 * Set the mask of all child fixture
	 * 
	 * @param entityCategory
	 * @return
	 */
	public RigidBody setCategoryBits(EntityCategory entityCategory) {
		CategoryBits = entityCategory.getValue();

		Fixture fixture = body.getFixtureList();
		while (fixture != null) {
			fixture.m_filter.categoryBits = CategoryBits;
			RigidFixutre rFixutre = (RigidFixutre) fixture.getUserData();
			rFixutre.CategoryBits = CategoryBits;
			fixture = fixture.getNext();
		}
		return this;
	}

	public RigidBody setMaskBits(EntityCategory entityCategory) {
		MaskBits = entityCategory.getValue();

		Fixture fixture = body.getFixtureList();
		while (fixture != null) {
			fixture.m_filter.maskBits = MaskBits;
			RigidFixutre rFixutre = (RigidFixutre) fixture.getUserData();
			rFixutre.MaskBits = MaskBits;
			fixture = fixture.getNext();
		}
		return this;
	}

	public RigidBody addMaskBits(EntityCategory entityCategory) {
		MaskBits = MaskBits | entityCategory.getValue();

		Fixture fixture = body.getFixtureList();
		while (fixture != null) {
			fixture.m_filter.maskBits = MaskBits;
			RigidFixutre rFixutre = (RigidFixutre) fixture.getUserData();
			rFixutre.MaskBits = MaskBits;
			fixture = fixture.getNext();
		}
		return this;
	}

	public RigidBody setVelocity(Vec2 v) {
		body.setLinearVelocity(v);
		return this;
	}

	public RigidBody AddFixture(RigidFixutre rigidFixutre, FixtureDef fixtureDef) {

		fixtureDef.filter.categoryBits = CategoryBits;
		fixtureDef.filter.maskBits = MaskBits;

		Fixture fixture = body.createFixture(fixtureDef);
		rigidFixutre.fixture = fixture;

		fixture.setUserData(rigidFixutre);

		return this;
	}

	public RigidBody RemoveFixture(RigidFixutre rigidFixutre) {
		Fixture fixture = body.getFixtureList();
		while (fixture != null) {
			if (fixture.equals(rigidFixutre.fixture)) {
				body.destroyFixture(fixture);
				return this;
			}

			fixture = fixture.getNext();
		}

		return this;
	}

	public RigidBody AddForce(Vector2f force) {
		body.applyForce(new Vec2(force.x, force.y), body.getWorldCenter());
		return this;
	}

	public RigidBody AddForce(Vec2 force) {
		body.applyForce(force, body.getWorldCenter());
		return this;
	}

	public RigidBody AddAngularForce(float rotationForce) {
		body.applyTorque(rotationForce);
		return this;
	}
}
