package engine.component.graphic.spriteRendererComponent;

import java.util.ArrayList;

import engine.component.graphic.FrameBufferObject;
import engine.component.graphic.SpriteRenderer;

public abstract class SpriteRendererComponent {
	public abstract void Render(int FrameBufferObjectID);

	public abstract void Update();

	public abstract void Start();

	protected int TextureOveride = -1;

	/**
	 * overide the parent spriteRenderer Texture
	 * 
	 * @param TextureID
	 */
	public void setTextureOveride(int TextureID) {
		TextureOveride = TextureID;
	}

	public SpriteRendererComponent() {
		Start();
	}

	public SpriteRenderer spriteRenderer;

	// which frame buffer to render to?
	public ArrayList<Integer> FrameBufferIDs = new ArrayList<>();

	public void Destroy() {
		spriteRenderer = null;
		if (FrameBufferIDs != null) {
			FrameBufferIDs.clear();
			FrameBufferIDs = null;
		}
	}

	public <T extends SpriteRendererComponent> T AddFrameBuffer(FrameBufferObject fbo) {
		FrameBufferIDs.add(fbo.FrameBufferID);
		return (T) this;
	}

	public <T extends SpriteRendererComponent> T SetFrameBuffer(FrameBufferObject fbo) {
		FrameBufferIDs.clear();
		FrameBufferIDs.add(fbo.FrameBufferID);
		return (T) this;
	}

	public <T extends SpriteRendererComponent> T RemoveFrameBuffer(FrameBufferObject fbo) {
		if (FrameBufferIDs.contains(fbo.FrameBufferID)) {
			FrameBufferIDs.remove(fbo.FrameBufferID);
		} else {
			throw new RuntimeException("SpriteRenderer does not contain the frame buffer IDs:" + fbo.FrameBufferID
					+ "that to be removed!");
		}
		return (T) this;
	}
}
