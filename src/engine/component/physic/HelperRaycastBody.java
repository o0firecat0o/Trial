package engine.component.physic;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class HelperRaycastBody implements RayCastCallback {

	public HelperRaycastBody(int EntityCategory) {
		this.EntityCategory = EntityCategory;
	}

	public Fixture targetfixture;
	public Vec2 point;
	public Vec2 normal;

	public final int EntityCategory;

	private float previousFraction = 1;

	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		RigidFixutre rigidFixutre = (RigidFixutre) fixture.getUserData();

		if (rigidFixutre.CategoryBits == EntityCategory) {
			targetfixture = fixture;
			this.point = point;
			this.normal = normal;
			previousFraction = fraction;
		}
		// the fraction will decrease every time a fixture with the mass
		// category bit is found, and stay the same if not found, hence, the
		// closest fixture could be found thisway
		return previousFraction;
	}

}
