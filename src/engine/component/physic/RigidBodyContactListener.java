package engine.component.physic;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

public class RigidBodyContactListener implements org.jbox2d.callbacks.ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		if (fa == null || fb == null) {
			return;
		}
		try {
			IContact callbackA = (IContact) fa.getUserData();
			callbackA.Contact(contact, fb);

			IContact callbackB = (IContact) fb.getUserData();
			callbackB.Contact(contact, fa);
		} catch (Exception e) {

		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold arg1) {
		// TODO Auto-generated method stub

	}

}
