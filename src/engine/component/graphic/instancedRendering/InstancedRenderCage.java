package engine.component.graphic.instancedRendering;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import engine.component.graphic.Shader;
import engine.component.graphic.Texture;
import engine.component.graphic.VertexArray;

public class InstancedRenderCage {
	public int TextureID;
	public int FrameBufferID;

	public VertexArray mesh;
	public int vbo;

	public static final int MAX_INSTANCES = 50000;
	public static final int INSTANCE_DATA_LENGTH = 20;

	private int pointer = 0;

	public final FloatBuffer BUFFER = org.lwjgl.BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH * 4);

	public InstancedRenderCage(int TextureID, int FrameBufferID) {
		this.TextureID = TextureID;
		this.FrameBufferID = FrameBufferID;

		float SIZE_X = 100 / 2;
		float SIZE_Y = 100 / 2;
		float[] vertices = new float[] { -SIZE_X, -SIZE_Y, 0f, -SIZE_X, SIZE_Y, 0f, SIZE_X, SIZE_Y, 0f, SIZE_X, -SIZE_Y,
				0f };
		byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
		float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };

		mesh = new VertexArray(vertices, indices, tcs);

		vbo = mesh.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		mesh.addInstancedAttribute(vbo, 2, 4, INSTANCE_DATA_LENGTH, 0);
		mesh.addInstancedAttribute(vbo, 3, 4, INSTANCE_DATA_LENGTH, 4);
		mesh.addInstancedAttribute(vbo, 4, 4, INSTANCE_DATA_LENGTH, 8);
		mesh.addInstancedAttribute(vbo, 5, 4, INSTANCE_DATA_LENGTH, 12);
		mesh.addInstancedAttribute(vbo, 6, 4, INSTANCE_DATA_LENGTH, 16);
	}

	public void Render() {

		if (instancedRenderObjects.size() > MAX_INSTANCES) {
			throw new RuntimeException(
					"MaxInstances Reached for texture:" + TextureID + "," + Texture.getTexture(TextureID));
		}

		pointer = 0;
		glActiveTexture(GL_TEXTURE1);
		GL11.glBindTexture(GL_TEXTURE_2D, TextureID);

		Shader shader = Shader.getShader("DefaultParticle");

		shader.enable();

		float[] vboDATA = new float[instancedRenderObjects.size() * INSTANCE_DATA_LENGTH];
		for (int i = 0; i < instancedRenderObjects.size(); i++) {
			InstancedRenderObject object = instancedRenderObjects.get(i);
			storeMatrixData(object.matrix4f, vboDATA);
			vboDATA[pointer++] = object.Color.x;
			vboDATA[pointer++] = object.Color.y;
			vboDATA[pointer++] = object.Color.z;
			vboDATA[pointer++] = object.Color.w;
		}

		updateVBO(vbo, vboDATA, BUFFER);

		mesh.bind();
		mesh.drawParticle(instancedRenderObjects.size());

		shader.disable();

		glBindTexture(GL_TEXTURE_2D, 0);

		instancedRenderObjects.clear();
	}

	public ArrayList<InstancedRenderObject> instancedRenderObjects = new ArrayList<>();

	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[pointer++] = matrix.m00();
		vboData[pointer++] = matrix.m01();
		vboData[pointer++] = matrix.m02();
		vboData[pointer++] = matrix.m03();
		vboData[pointer++] = matrix.m10();
		vboData[pointer++] = matrix.m11();
		vboData[pointer++] = matrix.m12();
		vboData[pointer++] = matrix.m13();
		vboData[pointer++] = matrix.m20();
		vboData[pointer++] = matrix.m21();
		vboData[pointer++] = matrix.m22();
		vboData[pointer++] = matrix.m23();
		vboData[pointer++] = matrix.m30();
		vboData[pointer++] = matrix.m31();
		vboData[pointer++] = matrix.m32();
		vboData[pointer++] = matrix.m33();
	}

	public void updateVBO(int vbo, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		if (vbo != -1) {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity(), GL15.GL_STATIC_DRAW);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
	}
}