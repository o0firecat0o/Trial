package engine.component.graphic.spriteRendererComponent;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.joml.*;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.*;
import engine.main.*;
import engine.math.*;
import engine.object.GameObject;

public class ShieldRenderer extends SpriteRendererComponent {

	@Override
	public void Update() {
		timer++;
		if (timer >= 600) {
			timer = 0;

		}
		if (timer % 4 == 0) {
			timer2++;
		}
		if (timer2 >= 60) {
			timer2 = 1;
		}
	}

	@Override
	public void Start() {
		SetFrameBuffer(Render.mainFrameBuffer2);

		timer = 0;
		timer2 = 1;
	}

	private float timer;
	private int timer2; // timer for the shield edge
	private float distortionValue = 0.01f;
	private float edgeThickness = 3.6f;
	private Vector3f shieldColor = new Vector3f(1, 1, 1);

	public ShieldRenderer setDistortionValue(float distortionValue) {
		this.distortionValue = Mathf.clamp(distortionValue, 0.000001f, 1000f);
		return this;
	}

	public ShieldRenderer setEdgeThickness(float edgeThickness) {
		this.edgeThickness = edgeThickness;
		return this;
	}

	public ShieldRenderer setShieldColor(Vector3f shieldColor) {
		this.shieldColor = shieldColor;
		return this;
	}

	@Override
	public void Render(int FrameBufferObjectID) {

		GL11.glEnable(GL11.GL_BLEND);
		// GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_ALPHA,
		// GL11.GL_ONE_MINUS_SRC_ALPHA);
		// org.lwjgl.opengl.ARBImaging.glBlendEquation(org.lwjgl.opengl.ARBImaging.GL_MIN);

		GameObject gameObject = spriteRenderer.gameObject;

		glActiveTexture(GL_TEXTURE1);
		GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("shield"));

		glActiveTexture(GL_TEXTURE2);
		GL11.glBindTexture(GL_TEXTURE_2D, Render.postBloomShader.FrameBufferID);

		glActiveTexture(GL_TEXTURE3);
		GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("rock_n"));

		glActiveTexture(GL_TEXTURE4);
		GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("sun (" + timer2 + ")"));

		Shader shader = Shader.getShader("Shield");

		shader.enable();

		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(gameObject.transform.position, gameObject.transform.rotation,
						new Vector2f(gameObject.transform.getScale().x * spriteRenderer.graphicScaleOffset,
								gameObject.transform.getScale().y * spriteRenderer.graphicScaleOffset)));

		shader.setUniform2f("ScreenSize", Main.getWidth(), Main.getHeight());
		shader.setUniform1f("timer", timer / 600f);
		shader.setUniform1f("distortionValue", distortionValue);
		shader.setUniform1f("edgeThickness", edgeThickness);
		shader.setUniform3f("shieldColor", shieldColor);

		SpriteRenderer.mesh_NORAML.render();

		shader.disable();

		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, 0);

		// reseting the glBlend
		org.lwjgl.opengl.ARBImaging.glBlendEquation(org.lwjgl.opengl.ARBImaging.GL_FUNC_ADD);
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void Destroy() {
		super.Destroy();
	}
}
