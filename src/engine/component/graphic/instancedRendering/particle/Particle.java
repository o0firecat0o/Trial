package engine.component.graphic.instancedRendering.particle;

import org.joml.Vector4f;

import engine.object.Transform;

public abstract class Particle {
	public final Transform transform = new Transform();

	public Vector4f color = new Vector4f(1, 1, 1, 1);

	public abstract void Update();

}