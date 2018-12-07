package engine.component.physic;

import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;

public class RayCastObject {
	public final Fixture fixture;
	public final Vector2f point;
	public final Vector2f normal;

	public RayCastObject(Fixture fixture, Vector2f point, Vector2f normal) {
		this.fixture = fixture;
		this.point = point;
		this.normal = normal;
	}
}
