package engine.component.physic;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.main.Logic;
import engine.math.Mathf;

public class Physics {
	public static Map<Fixture, org.jbox2d.dynamics.contacts.Contact> OverlapCircle(Vector3f position, float radius,
			int MaskedBit) {
		HelperPhysicBody helperPhysicBody = new HelperPhysicBody(position, radius, MaskedBit);
		Logic.world.step(0f, 8, 3);
		Map<Fixture, org.jbox2d.dynamics.contacts.Contact> returnList = helperPhysicBody.returnContacts();
		helperPhysicBody.Destroy();
		return returnList;
	}

	public static Fixture[] OverlapCircleBasic(Vector3f position, float radius, int MaskedBit) {
		HelperPhysicBody helperPhysicBody = new HelperPhysicBody(position, radius, MaskedBit);
		Logic.world.step(0f, 8, 3);
		Set<Fixture> fixtures = helperPhysicBody.returnContacts().keySet();
		helperPhysicBody.Destroy();
		return fixtures.toArray(new Fixture[fixtures.size()]);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Fixture> OverlapAABB(Vector2f a, Vector2f b) {
		// return back to the correct physic ratio
		a.mul(1f / RigidBody.RATIO);
		b.mul(1f / RigidBody.RATIO);
		HelperAABBQueryBody aabbQueryBody = new HelperAABBQueryBody();
		if (a.x > b.x) {
			float t = b.x;
			b.x = a.x;
			a.x = t;
		}
		if (a.y > b.y) {
			float t = b.y;
			b.y = a.y;
			a.y = t;
		}
		AABB aabb = new AABB(Mathf.Vector2ftoVec2(a), Mathf.Vector2ftoVec2(b));
		Logic.world.queryAABB(aabbQueryBody, aabb);
		ArrayList<Fixture> fixtureList = (ArrayList<Fixture>) aabbQueryBody.fixtureList.clone();
		aabbQueryBody.Destory();
		return fixtureList;
	}

	public static RayCastObject RayCasting(Vector2f point1, Vector2f point2, int EntityCategory) {

		// converting the logic position back to the physic position
		Vec2 pointA = new Vec2(point1.x / RigidBody.RATIO, point1.y / RigidBody.RATIO);
		Vec2 pointB = new Vec2(point2.x / RigidBody.RATIO, point2.y / RigidBody.RATIO);

		HelperRaycastBody raycastBody = new HelperRaycastBody(EntityCategory);

		try {
			Logic.world.raycast(raycastBody, pointA, pointB);
		} catch (NullPointerException e) {
			return null;
		}

		if (raycastBody.targetfixture != null) {
			return new RayCastObject(raycastBody.targetfixture, Mathf.Vec2toVector2f(raycastBody.point.mul(100)),
					Mathf.Vec2toVector2f(raycastBody.normal.mul(100)));
		} else {
			return null;
		}
	}

}
