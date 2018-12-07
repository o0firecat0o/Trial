package engine.component.graphic;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;

import org.lwjgl.opengl.*;

public class FrameBufferObject {

	public int FrameBufferID;
	public int colorTextureID;
	public int depthRenderBufferID;

	public FrameBufferObject(int Width, int Height) {
		// check if FBO is enable
		boolean FBOEnabled = GL.createCapabilities().GL_EXT_framebuffer_object;
		System.out.println("FBO capabilities is " + FBOEnabled);

		FrameBufferID = glGenFramebuffersEXT(); // create a new framebuffer
		colorTextureID = glGenTextures(); // And finally a new depthbuffer
		depthRenderBufferID = glGenRenderbuffersEXT(); // And finally a new
														// depthbuffer

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FrameBufferID); // switch to
																	// the new
																	// framebuffer

		// initialize color texture
		glBindTexture(GL_TEXTURE_2D, colorTextureID); // Bind the colorbuffer
														// texture
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); // make
																			// it
																			// linear
																			// filterd
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, Width, Height, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null); // Create
																													// the
																													// texture
																													// data
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, colorTextureID, 0); // attach

		// initialize depth renderbuffer
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind
																			// the
																			// depth
																			// renderbuffer
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, Width, Height); // get
		// the
		// data
		// space
		// for
		// it
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
				depthRenderBufferID); // bind it to the renderbuffer

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); // Swithch back to normal
														// framebuffer rendering
	}
}
