package engine.component.physic;

import java.util.*;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.*;
import org.joml.Vector3f;

import engine.main.Logic;

public class HelperPhysicBody implements IContact {
	Body body;

	/**
	 * Remember to add mask "HELPER" to the body that you wanna detect
	 * 
	 * @param position
	 * @param radius
	 * @param MaskedBit
	 */
	public HelperPhysicBody(Vector3f position, float radius, int MaskedBit) {

		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position.x / RigidBody.RATIO, position.y / RigidBody.RATIO);
		bodyDef.type = BodyType.DYNAMIC;

		body = Logic.world.createBody(bodyDef);

		FixtureDef bodyFixture = new FixtureDef();
		bodyFixture = new FixtureDef();
		bodyFixture.density = 1;

		CircleShape cShape = new CircleShape();
		cShape.m_radius = radius;
		bodyFixture.shape = cShape;

		bodyFixture.filter.categoryBits = EntityCategory.HELPER.getValue();
		bodyFixture.filter.maskBits = MaskedBit;

		body.createFixture(bodyFixture).setUserData(this);
	}

	private Map<Fixture, org.jbox2d.dynamics.contacts.Contact> Contacts = new HashMap<>();

	@Override
	public void Contact(org.jbox2d.dynamics.contacts.Contact contact, Fixture target) {
		Contacts.put(target, contact);
	}

	public Map<Fixture, org.jbox2d.dynamics.contacts.Contact> returnContacts() {
		return Contacts;
	}

	public void Destroy() {
		Contacts = null;
		Logic.world.destroyBody(body);
		body.setUserData(null);
		body = null;
	}
}
