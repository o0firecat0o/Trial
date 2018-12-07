package engine.component.graphic.spriteRendererComponent;

import org.joml.*;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.*;
import engine.main.Render;
import engine.math.Maths;
import engine.object.GameObject;

public class CircularMeterRenderer extends SpriteRendererComponent {

	private float precentage = 1;
	private float thickness = 0.1f;
	private final Vector3f Color = new Vector3f(1, 1, 1);
	private float innerDiameter = 0.5f;

	/**
	 * setting the angle
	 * 
	 * @param angle
	 *            from 0 to 1;
	 * @return
	 */
	public CircularMeterRenderer setPrecentage(float precentage) {
		this.precentage = precentage;
		return this;
	}

	/**
	 * setting the thickness of the line
	 * 
	 * @param thickness
	 *            from 0 to 0.5
	 * @return
	 */
	public CircularMeterRenderer setThickness(float thickness) {
		this.thickness = thickness;
		return this;
	}

	/**
	 * setting the inner diameter of the line
	 * 
	 * @param Color
	 * @return
	 */
	public CircularMeterRenderer setInnerDiameter(float innerDiameter) {
		this.innerDiameter = innerDiameter;
		return this;
	}

	public CircularMeterRenderer setColor(Vector3f Color) {
		this.Color.x = Color.x;
		this.Color.y = Color.y;
		this.Color.z = Color.z;
		return this;
	}

	@Override
	public void Render(int FrameBufferObjectID) {
		GL11.glDisable(GL11.GL_BLEND);

		Shader shader;

		GameObject gameObject = spriteRenderer.gameObject;

		shader = Shader.getShader("CircularMeter");

		shader.enable();
		if (gameObject == null || gameObject.transform == null) {
			return;
		}
		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(gameObject.transform.position, gameObject.transform.rotation,
						new Vector2f(gameObject.transform.getScale().x * spriteRenderer.graphicScaleOffset,
								gameObject.transform.getScale().y * spriteRenderer.graphicScaleOffset)));

		shader.setUniform1f("precentage", precentage);
		shader.setUniform1f("thickness", thickness);
		shader.setUniform3f("Color", Color);
		shader.setUniform1f("innerDiameter", innerDiameter);

		SpriteRenderer.mesh_NORAML.render();

		shader.disable();
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		AddFrameBuffer(Render.mainFrameBuffer);
	}

}
