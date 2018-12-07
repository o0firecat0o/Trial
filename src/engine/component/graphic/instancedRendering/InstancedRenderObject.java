package engine.component.graphic.instancedRendering;

import org.joml.*;

public class InstancedRenderObject {
	public Matrix4f matrix4f;
	public Vector4f Color;

	public InstancedRenderObject(Matrix4f position, Vector4f Color) {
		this.matrix4f = position;
		this.Color = Color;
	}
}