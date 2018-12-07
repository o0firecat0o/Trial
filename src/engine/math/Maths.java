package engine.math;

import org.joml.*;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rz, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation);
		matrix.rotate(rz, new Vector3f(0, 0, 1));
		matrix.scale(scale.x, scale.y, 1);
		return matrix;
	}

	/**
	 * used in rigidFixture, to calculate the normals of the shape for collision
	 * repulsion
	 * 
	 * @param vertices
	 * @return
	 */
	public static Vector2f[] createNormalfromVertices(Vector2f[] vertices) {
		// assign a new tempVertices array for better management
		Vector2f[] tempVertices = new Vector2f[vertices.length + 1];

		// return vector[]
		Vector2f[] returnNormals = new Vector2f[vertices.length];

		// transfer all the vertices into the temp array
		for (int i = 0; i < vertices.length; i++) {
			tempVertices[i] = vertices[i];
		}

		// 0->n
		// e.g. for a original vertices of 20 elements
		// vertices[20] = vertices[0]
		tempVertices[vertices.length] = vertices[0];

		for (int i = 0; i < vertices.length; i++) {
			// https://gamedev.stackexchange.com/questions/26951/calculating-the-2d-edge-normals-of-a-triangle

			// imaging a triangle of point A,B,C
			// we need to find a normal for edge AB

			Vector2f AB = new Vector2f(tempVertices[i + 1].x - tempVertices[i].x,
					tempVertices[i + 1].y - tempVertices[i].y);

			// N = normalized(AB)
			Vector2f N = new Vector2f(AB.y, -AB.x);

			returnNormals[i] = N;
		}

		return returnNormals;
	}
}
