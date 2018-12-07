package org.Stage;

import org.joml.Vector3f;
import org.joml.Vector4f;

import engine.component.graphic.Texture;
import engine.component.graphic.instancedRendering.InstancedRenderObject;
import engine.component.graphic.instancedRendering.InstancedRenderer;
import engine.main.Render;
import engine.math.Maths;
import engine.object.Component;

public class PaddleKey extends Component {

	@Override
	protected void Update() {
		gameObject.transform.setRotation(gameObject.transform.getRotation() + 0.003f);

		alpha -= 0.1f;
		if (alpha < 0) {
			gameObject.InitDestroy();
		}
	}

	@Override
	public void UpdateRender() {
		InstancedRenderObject iro = new InstancedRenderObject(
				Maths.createTransformationMatrix(gameObject.transform.position, gameObject.transform.getRotation(),
						gameObject.transform.scale),
				new Vector4f(Color, alpha));
		InstancedRenderer.Add(iro, Texture.getTexture("PaddleKey"), Render.mainFrameBuffer.FrameBufferID);
		super.UpdateRender();
	}

	private static final Vector3f Color = new Vector3f(1, 1, 1);

	private float alpha = 1;

	@Override
	protected void Start() {
		// TODO Auto-generated method stub

	}

}
