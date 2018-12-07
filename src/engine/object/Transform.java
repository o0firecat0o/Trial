package engine.object;

import org.jbox2d.common.Vec2;
import org.joml.*;
import org.joml.Math;

import engine.math.Mathf;

public class Transform {
	public final Vector3f position = new Vector3f();
	public float rotation = 0;
	public final Vector2f scale = new Vector2f(1f, 1f);

	public void setPosition(Vector3f position) {
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}

	public void setPosition(float z) {
		this.position.z = z;
	}

	public void setPosition(Vector2f position) {
		this.position.x = position.x;
		this.position.y = position.y;
	}

	public void addPosition(Vector2f position) {
		this.position.x += position.x;
		this.position.y += position.y;
	}

	public Vector2f getPositionVector2f() {
		return new Vector2f(position.x, position.y);
	}

	public Vec2 getPositionVec2() {
		return new Vec2(position.x, position.y);
	}

	/*
	 * distance = 100
	 */
	public Vector2f getPositionForward(float distance) {
		return getPositionVector2f().add(Mathf.Vector2fFromAngle(getRotation()).mul(distance));
	}

	/**
	 * Get the rotation in -3.14 to +3.14 radians
	 * 
	 * @return
	 */
	public float getRotation() {
		float returnFloat = rotation;
		while (returnFloat < -Math.PI) {
			returnFloat += Math.PI * 2;
		}
		while (returnFloat > Math.PI) {
			returnFloat -= Math.PI * 2;
		}
		return returnFloat;
	}

	/*
	 * set the rotation, in radians
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setScale(Vector2f scale) {
		this.scale.x = scale.x;
		this.scale.y = scale.y;
	}

	public void setScale(float scale) {
		this.scale.x = scale;
		this.scale.y = scale;
	}

	public Vector2f getScale() {
		return new Vector2f(scale);
	}
}
