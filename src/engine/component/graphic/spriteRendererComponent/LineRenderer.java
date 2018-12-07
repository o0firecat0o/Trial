package engine.component.graphic.spriteRendererComponent;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.GL_TEXTURE6;
import static org.lwjgl.opengl.GL13.GL_TEXTURE7;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.*;
import org.joml.Math;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.*;
import engine.main.Render;
import engine.math.Maths;

public class LineRenderer extends SpriteRendererComponent {

	public enum LineRenderType {
		lazer_straight, lazer_curved
	}

	private Vector2f startpoint;
	private Vector2f endpoint;
	private float width = 0.25f;
	private boolean fade = true;
	private float alpha = 1f;
	private float fadeTime = 120;

	private LineRenderType lineRenderType = LineRenderType.lazer_straight;

	@Override
	public void Update() {
		timer++;
		if (timer2 > fadeTime) {
			spriteRenderer.gameObject.InitDestroy();
		}
		if (fade) {
			timer2++;
			alpha = 1 - (timer2 / fadeTime) * (timer2 / fadeTime);
		}
	}

	float timer = 0;
	float timer2 = 0;

	@Override
	public void Render(int FrameBufferObjectID) {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		Vector2f midpoint = new Vector2f((startpoint.x + endpoint.x) / 2, (startpoint.y + endpoint.y) / 2);
		Vector2f directionVector = new Vector2f(endpoint.x - startpoint.x, endpoint.y - startpoint.y);
		float rotation = (float) Math.atan2(directionVector.y, directionVector.x);
		float distance = startpoint.distance(endpoint);

		if (lineRenderType == LineRenderType.lazer_straight) {
			Shader shader;
			shader = Shader.getShader("Lazer");

			glActiveTexture(GL_TEXTURE1);
			GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("laser_m"));

			glActiveTexture(GL_TEXTURE2);
			GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("laser_m_o"));

			glActiveTexture(GL_TEXTURE3);
			GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("laser_s"));

			glActiveTexture(GL_TEXTURE4);
			GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("laser_s_o"));

			glActiveTexture(GL_TEXTURE5);
			GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("laser_e"));

			glActiveTexture(GL_TEXTURE6);
			GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("laser_e_o"));

			glActiveTexture(GL_TEXTURE7);
			GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("laser_n"));

			shader.enable();

			shader.setUniformMat4f("ml_matrix", Maths.createTransformationMatrix(
					new Vector3f(midpoint.x, midpoint.y, 0), rotation, new Vector2f(distance / 100f, width)));

			shader.setUniform1f("timer", timer / 120);
			shader.setUniform1f("alpha", alpha);
			shader.setUniform3f("LazerColor", new Vector3f(0.259f, 1.0f, 1.0f));
			shader.setUniform1f("relativeScale", 100 * width / distance);

			SpriteRenderer.mesh_NORAML.render();

			shader.disable();
			glBindTexture(GL_TEXTURE_2D, 0);
		}

		if (lineRenderType == LineRenderType.lazer_curved) {
			Shader shader;
			shader = Shader.getShader("LazerCurved");

			glActiveTexture(GL_TEXTURE1);
			// GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("Sprite0"));

			shader.enable();

			shader.setUniformMat4f("ml_matrix", Maths.createTransformationMatrix(
					new Vector3f(midpoint.x, midpoint.y, 0), rotation, new Vector2f(distance / 100f, width)));

			shader.setUniform1f("timer", timer / 120f);
			SpriteRenderer.mesh_NORAML.render();

			shader.disable();
			glBindTexture(GL_TEXTURE_2D, 0);
		}

	}

	public LineRenderer setPoints(Vector2f start, Vector2f end) {
		startpoint = start;
		endpoint = end;
		return this;
	}

	public LineRenderer setWidth(float width) {
		this.width = width;
		return this;
	}

	public LineRenderer setFade(boolean isFade) {
		fade = isFade;
		return this;
	}

	public LineRenderer setFadeTime(float fadeTime) {
		this.fadeTime = fadeTime;
		return this;
	}

	public LineRenderer setLineRenderType(LineRenderType lineRenderType) {
		this.lineRenderType = lineRenderType;
		return this;
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		SetFrameBuffer(Render.mainFrameBuffer);
	}

}
