package engine.component.graphic.instancedRendering.particle;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.instancedRendering.InstancedRenderObject;
import engine.component.graphic.instancedRendering.InstancedRenderer;
import engine.component.graphic.spriteRendererComponent.SpriteRendererComponent;
import engine.main.Render;
import engine.math.Maths;

public abstract class ParticleRenderer extends SpriteRendererComponent {

	public ArrayList<Particle> Particles;

	@Override
	public void Render(int FrameBufferObjectID) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		List<InstancedRenderObject> instancedRenderObjects = new ArrayList<>();

		for (int i = 0; i < Particles.size(); i++) {
			if (i > Particles.size()) {
				break;
			}
			Particle particle = Particles.get(i);
			// if the particle has already been destroyed;
			if (particle == null) {
				continue;
			}
			Matrix4f matrix4f = Maths.createTransformationMatrix(particle.transform.position,
					particle.transform.rotation, particle.transform.scale);
			instancedRenderObjects.add(new InstancedRenderObject(matrix4f, particle.color));

		}
		// if the texture is not intended to overide
		if (TextureOveride == -1) {
			InstancedRenderer.Add(instancedRenderObjects, spriteRenderer.texture, FrameBufferObjectID);
		} else {
			InstancedRenderer.Add(instancedRenderObjects, TextureOveride, FrameBufferObjectID);
		}

		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void Update() {

		for (int i = 0; i < Particles.size(); i++) {
			Particles.get(i).Update();
		}
		CustomUpdate();
	}

	public abstract void CustomUpdate();

	@Override
	public void Start() {
		Particles = new ArrayList<>();
		SetFrameBuffer(Render.mainFrameBuffer);
	}

	@Override
	public void Destroy() {
		Particles.clear();
		super.Destroy();
	}
}
