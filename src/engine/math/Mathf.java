package engine.math;

import org.jbox2d.common.Vec2;
import org.joml.*;
import org.joml.Math;

import engine.object.GameObject;

public class Mathf {
	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}

	public static Vector2f interpolation(Vector2f first, Vector2f second, float percentage) {
		float x = first.x() * percentage + second.x() * (1 - percentage);
		float y = first.y() * percentage + second.y() * (1 - percentage);
		return new Vector2f(x, y);
	}

	/**
	 * 
	 * @param vectorthe
	 *            vector to be rotated
	 * @param angle
	 *            angle in radians
	 * @return
	 */
	public static Vector2f rotateVector(Vector2f vector, float angle) {
		Vector2f returnVector = new Vector2f();
		returnVector.x = (float) (vector.x * Math.cos(angle) - vector.y * Math.sin(angle));
		returnVector.y = (float) (vector.x * Math.sin(angle) + vector.y * Math.cos(angle));
		return returnVector;
	}

	/**
	 * 
	 * @param vector
	 * @param angle
	 *            angle in radians
	 * @param origin
	 *            the vector is rotate around this origin
	 * @return
	 */
	public static Vector2f rotateVectorAroundPoint(Vector2f vector, float angle, Vector2f origin) {
		Vector2f returnVector = new Vector2f();
		// translate the vector to a coordinate system in which the center of
		// rotation is (0,0)
		returnVector.add(vector).sub(origin);
		// apply the rotation
		returnVector = rotateVector(returnVector, angle);
		// translate the vector back to the original coordinate system
		returnVector.add(origin);
		return returnVector;
	}

	public static Vector2i Vector2ftoVector2i(Vector2f vector) {
		return new Vector2i((int) vector.x, (int) vector.y);
	}

	public static Vector2f Vec2toVector2f(Vec2 vector) {
		return new Vector2f(vector.x, vector.y);
	}

	public static Vec2 Vector2ftoVec2(Vector2f vector) {
		return new Vec2(vector.x, vector.y);
	}

	public static Vector2f addVector(Vector2f a, Vector2f b) {
		return new Vector2f(a.x + b.x, a.y + b.y);
	}

	public static Vector2f minusVector(Vector2f a, Vector2f b) {
		return new Vector2f(a.x - b.x, a.y - b.y);
	}

	/**
	 * return distance without z component
	 * 
	 * @param gameObject1
	 * @param gameObject2
	 * @return
	 */
	public static float Distance(GameObject gameObject1, GameObject gameObject2) {
		float d = gameObject1.transform.getPositionVector2f().distance(gameObject2.transform.getPositionVector2f());
		return d;
	}

	public static Vector2f VectorDistance(GameObject gameObject1, GameObject gameObject2) {
		return new Vector2f(gameObject1.transform.position.x - gameObject2.transform.position.x,
				gameObject1.transform.position.y - gameObject2.transform.position.y);
	}

	/**
	 * 
	 * @param angle
	 *            In Radians, just put in gameObject.transform.rotation
	 * @return
	 */
	public static Vector2f Vector2fFromAngle(float angle) {
		float myAngleInRadians = (angle);
		Vector2f angleVector = new Vector2f(-(float) Math.sin(myAngleInRadians), +(float) Math.cos(myAngleInRadians));
		return angleVector;
	}

	public static float AngleFromVector2f(Vector2f vector2f) {
		float angleFromVector = (float) Math.atan2(-vector2f.x, vector2f.y);
		return angleFromVector;
	}

	/**
	 * return the direction that you need to turn if you want to archieve target
	 * angel from current angle
	 * 
	 * @param CurrentAngle
	 *            in radian
	 * @param TargetAngle
	 *            in radian
	 * @return 0 -> 3.14, need clockwise rotation; 0 -> -3.14 need anticlockwise
	 *         rotation
	 */
	public static float returnDirection(float CurrentAngle, float TargetAngle) {
		float direcationAngle = CurrentAngle - TargetAngle;
		while (direcationAngle > Math.PI) {
			direcationAngle -= Math.PI * 2;
		}
		while (direcationAngle < -Math.PI) {
			direcationAngle += Math.PI * 2;
		}
		return direcationAngle;
	}
}
