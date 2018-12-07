package engine.component.graphic.spriteRendererComponent;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.Shader;
import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.Texture;
import engine.main.Render;
import engine.math.Maths;

public class LineRenderer_Simple extends SpriteRendererComponent {

	private final Vector2f startpoint = new Vector2f();
	private final Vector2f endpoint = new Vector2f();
	private final Vector3f Color = new Vector3f(1, 1, 1);
	private float alpha = 1;
	private float width = 1f;

	@Override
	public void Render(int FrameBufferObjectID) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Vector2f midpoint = new Vector2f((startpoint.x + endpoint.x) / 2, (startpoint.y + endpoint.y) / 2);
		Vector2f directionVector = new Vector2f(endpoint.x - startpoint.x, endpoint.y - startpoint.y);
		float rotation = (float) Math.atan2(directionVector.y, directionVector.x);
		float distance = startpoint.distance(endpoint);

		Shader shader;
		shader = Shader.getShader("DEFAULT");

		glActiveTexture(GL_TEXTURE1);
		GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("laser_m_o"));

		shader.enable();

		shader.setUniformMat4f("ml_matrix", Maths.createTransformationMatrix(new Vector3f(midpoint.x, midpoint.y, 0),
				rotation, new Vector2f(distance / 100f, width)));
		shader.setUniform3f("colorTaint", Color);
		shader.setUniform1f("alphaTaint", alpha);

		SpriteRenderer.mesh_NORAML.render();

		shader.disable();
		glBindTexture(GL_TEXTURE_2D, 0);

		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		SetFrameBuffer(Render.mainFrameBuffer);
	}

	public LineRenderer_Simple setPoints(Vector2f start, Vector2f end) {
		startpoint.x = start.x;
		startpoint.y = start.y;
		endpoint.x = end.x;
		endpoint.y = end.y;
		return this;
	}

	public LineRenderer_Simple setWidth(float width) {
		this.width = width;
		return this;
	}

	public LineRenderer_Simple setColor(float r, float g, float b) {
		Color.x = r;
		Color.y = g;
		Color.z = b;
		return this;
	}

	public LineRenderer_Simple setAlpha(float alpha) {
		this.alpha = alpha;
		return this;
	}

}
