package engine.component.graphic.spriteRendererComponent;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.*;
import engine.main.Render;
import engine.math.Maths;
import engine.object.*;

public class BloomRenderer extends SpriteRendererComponent {

	@Override
	public void Update() {

	}

	@Override
	public void Start() {
		SetFrameBuffer(Render.bloomFrameBuffer);
	}

	@Override
	public void Render(int FrameBufferObjectID) {
		Shader shader;

		GameObject gameObject = spriteRenderer.gameObject;

		if (gameObject instanceof UIObject) {
			shader = Shader.getShader("UI");
		} else {
			shader = Shader.getShader("DEFAULT");
		}

		GL11.glBindTexture(GL_TEXTURE_2D, spriteRenderer.texture);

		shader.enable();
		if (gameObject == null || gameObject.transform == null) {
			return;
		}
		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(gameObject.transform.position, gameObject.transform.rotation,
						new Vector2f(gameObject.transform.getScale().x * spriteRenderer.graphicScaleOffset,
								gameObject.transform.getScale().y * spriteRenderer.graphicScaleOffset)));

		SpriteRenderer.mesh_NORAML.render();

		shader.disable();
		glBindTexture(GL_TEXTURE_2D, 0);
	}

}
