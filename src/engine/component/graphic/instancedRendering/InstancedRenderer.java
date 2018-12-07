package engine.component.graphic.instancedRendering;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

public class InstancedRenderer {

	public static void Render(int FrameBufferID) {

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		for (int i = 0; i < instancedRenderCages.size(); i++) {
			if (instancedRenderCages.get(i).FrameBufferID == FrameBufferID) {
				instancedRenderCages.get(i).Render();
			}
		}

		glDisable(GL_BLEND);
	}

	private static List<InstancedRenderCage> instancedRenderCages = new ArrayList<>();

	/**
	 * This function MUST be called in the render thread
	 * 
	 * @param instancedRenderObjects
	 * @param TextureID
	 * @param FrameBufferID
	 */
	public static void Add(List<InstancedRenderObject> instancedRenderObjects, int TextureID, int FrameBufferID) {
		InstancedRenderCage irc = getInstancedRenderCage(TextureID, FrameBufferID);
		if (irc == null) {
			irc = NewInstancedRenderCage(TextureID, FrameBufferID);
		}
		irc.instancedRenderObjects.addAll(instancedRenderObjects);
	}

	/**
	 * This function MUST be called in the render thread
	 * 
	 * @param instancedRenderObjects
	 * @param TextureID
	 * @param FrameBufferID
	 */
	public static void Add(InstancedRenderObject instancedRenderObject, int TextureID, int FrameBufferID) {
		InstancedRenderCage irc = getInstancedRenderCage(TextureID, FrameBufferID);
		if (irc == null) {
			irc = NewInstancedRenderCage(TextureID, FrameBufferID);
		}
		irc.instancedRenderObjects.add(instancedRenderObject);
	}

	private static InstancedRenderCage NewInstancedRenderCage(int TextureID, int FrameBufferID) {
		InstancedRenderCage instancedRenderCage = new InstancedRenderCage(TextureID, FrameBufferID);
		System.out.println("New Instanced Render Cage for Texture:" + TextureID + ", frameBuffer:" + FrameBufferID);
		instancedRenderCages.add(instancedRenderCage);
		return instancedRenderCage;
	}

	private static InstancedRenderCage getInstancedRenderCage(int TextureID, int FrameBufferID) {
		for (int i = 0; i < instancedRenderCages.size(); i++) {
			if (instancedRenderCages.get(i).FrameBufferID == FrameBufferID
					&& instancedRenderCages.get(i).TextureID == TextureID) {
				return instancedRenderCages.get(i);
			}
		}
		return null;
	}

}
