package engine.component.graphic;

import java.util.ArrayList;

import org.joml.Matrix4f;

import engine.component.graphic.spriteRendererComponent.SpriteRendererComponent;
import engine.main.Main;
import engine.object.Component;

public class SpriteRenderer extends Component {

	public static Matrix4f pr_matrix = new Matrix4f().ortho(0, Main.getWidth(), 0, Main.getHeight(), -10, 10);

	public static ArrayList<SpriteRenderer> allSpriteRenderer = new ArrayList<>();

	public static void loadDefault() {
		float SIZE_X = 100 / 2;
		float SIZE_Y = 100 / 2;
		float[] vertices = new float[] { -SIZE_X, -SIZE_Y, 0f, -SIZE_X, SIZE_Y, 0f, SIZE_X, SIZE_Y, 0f, SIZE_X, -SIZE_Y,
				0f };
		byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
		float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };

		mesh_NORAML = new VertexArray(vertices, indices, tcs);
	}

	public static void loadDefault2() {
		float SIZE_X = 1f;
		float SIZE_Y = 1f;
		float[] vertices = new float[] { 0, 0, 0f, 0, SIZE_Y, 0f, SIZE_X, SIZE_Y, 0f, SIZE_X, 0, 0f };
		byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
		float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };

		mesh_FULSCREEN = new VertexArray(vertices, indices, tcs);
	}

	public static VertexArray mesh_NORAML;
	public static VertexArray mesh_FULSCREEN;

	private ArrayList<SpriteRendererComponent> spriterenderercomponents = new ArrayList<>();

	public int texture;

	// scaling by NOT following the gameobject scale
	public float graphicScaleOffset = 1f;

	public SpriteRenderer() {
		allSpriteRenderer.add(this);
	}

	@Override
	protected void Start() {

	}

	// This function will be called in the render loop
	public void render(int FrameBufferObjectID) {
		for (int i = 0; i < spriterenderercomponents.size(); i++) {
			SpriteRendererComponent component = spriterenderercomponents.get(i);
			if (component.FrameBufferIDs == null) {
				continue;
			}
			if (component.FrameBufferIDs.contains(FrameBufferObjectID)) {
				component.Render(FrameBufferObjectID);
			}
		}
	}

	@Override
	protected void Destroy() {
		// OLD
		/*
		 * clean up the glsl memory if (mesh != null) {
		 * GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); GL15.glDeleteBuffers(mesh.vbo);
		 *
		 * GL30.glBindVertexArray(0); GL30.glDeleteVertexArrays(mesh.vao); } mesh =
		 * null;
		 */
		allSpriteRenderer.remove(this);

		for (int i = 0; i < spriterenderercomponents.size(); i++) {
			spriterenderercomponents.get(i).Destroy();
		}

		super.Destroy();
	}

	public SpriteRenderer SetTexture(int texture) {
		this.texture = texture;
		return this;
	}

	public SpriteRenderer SetGraphicScaleOffset(float graphicScaleOffset) {
		this.graphicScaleOffset = graphicScaleOffset;
		return this;
	}

	@Override
	protected void Update() {
		for (int i = 0; i < spriterenderercomponents.size(); i++) {
			spriterenderercomponents.get(i).Update();
		}
	}

	public <T extends SpriteRendererComponent> T addSpriteRendererComponent(T spriterenderercomponent) {
		spriterenderercomponents.add(spriterenderercomponent);
		spriterenderercomponent.spriteRenderer = this;
		return spriterenderercomponent;
	}

	@SuppressWarnings("unchecked")
	public <T extends SpriteRendererComponent> T getSpriteRendererComponent(Class<T> ComponentClass) {
		for (int i = 0; i < spriterenderercomponents.size(); i++) {
			if (spriterenderercomponents.get(i).getClass().equals(ComponentClass)) {
				return (T) spriterenderercomponents.get(i);
			}
		}
		return null;
	}
}
