package engine.main;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;
import org.opencv.core.Core;

import engine.component.Camera;
import engine.component.graphic.FrameBufferObject;
import engine.component.graphic.Shader;
import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.Texture;
import engine.component.graphic.instancedRendering.InstancedRenderer;
import engine.component.sound.Sound;
import engine.font.FontRenderer;
import engine.input.InputKey;
import engine.input.InputMouseButton;
import engine.input.InputMousePos;
import engine.input.InputMouseScroll;
import engine.input.InputText;
import engine.math.Maths;
import engine.object.Component;
import engine.object.GameObject;
import engine.object.UpdatableObject;

public class Render implements Runnable {

	public static boolean running = false;
	public static long window;

	public static boolean finishInit = false;

	public static long fps;

	public static FrameBufferObject bloomFrameBuffer; // For bloom effect
	public static FrameBufferObject mainFrameBuffer;
	public static FrameBufferObject mainFrameBuffer2; // frame buffer after all
														// distortion
	private static FrameBufferObject firstBlur;
	public static FrameBufferObject glowFrameBuffer; // Glow

	public static FrameBufferObject postBloomShader;

	public static FrameBufferObject rippleDistortion;

	private static long ALdevice;

	private void init() {
		if (glfwInit() != true) {
			return;
		}
		// set resizeable to false
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		// make the program use the highest OpenGL version possible
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		window = glfwCreateWindow(Main.getWidth(), Main.getHeight(), "Tower Defense Evovled", NULL, NULL);

		if (window == NULL) {
			return;
		}

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - Main.getWidth()) / 2, (vidmode.height() - Main.getHeight()) / 2);

		// set input keyboard
		glfwSetKeyCallback(window, new InputKey());
		// set input text
		glfwSetCharCallback(window, new InputText());
		// set input mouse position
		glfwSetCursorPosCallback(window, new InputMousePos());
		// set input mouse key
		glfwSetMouseButtonCallback(window, new InputMouseButton());
		// set input mouse scroll
		glfwSetScrollCallback(window, new InputMouseScroll());

		glfwSwapInterval(1); // enable vysnc, displaying 60fps

		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();

		glClearDepth(1.0f); // Depth Buffer Setup
		glDepthFunc(GL_LEQUAL); // The Type Of Depth Testing (Less Or Equal)
		glEnable(GL_DEPTH_TEST); // Enable Depth Testing
		System.out.println("OpenGL: " + glGetString(GL_VERSION));

		// enalbe transpatency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// The texture is now in channel 1
		glActiveTexture(GL_TEXTURE1);
		// Load all of the default shader
		Shader.loadDefault();

		// Set the Background Color to Black?
		GLCLEAR(new Vector4f(0f, 0f, 0f, 1.0f));

		// Create a FBO
		bloomFrameBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		mainFrameBuffer = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		mainFrameBuffer2 = new FrameBufferObject(Main.getWidth(), Main.getHeight());
		rippleDistortion = new FrameBufferObject(Main.getWidth(), Main.getHeight());

		postBloomShader = new FrameBufferObject(Main.getWidth(), Main.getHeight());

		// Creating an FBO with smaller size
		// remeber to set the viewport to smaller also
		firstBlur = new FrameBufferObject(Main.getHeight(), Main.getWidth());
		glowFrameBuffer = new FrameBufferObject(Main.getHeight(), Main.getWidth());

		// init TextMaster
		FontRenderer.init();

		// load default shaders
		SpriteRenderer.loadDefault();
		SpriteRenderer.loadDefault2();
		// load default image textures
		Texture.LoadDefault(new File("res/Sprites"));

		// sound
		ALdevice = ALC10.alcOpenDevice((ByteBuffer) null);
		if (ALdevice == NULL) {
			throw new IllegalStateException("Failed to open the default OpenAL device.");
		}
		ALCCapabilities deviceCaps = ALC.createCapabilities(ALdevice);
		long context = ALC10.alcCreateContext(ALdevice, (IntBuffer) null);
		if (context == NULL) {
			throw new IllegalStateException("Failed to create OpenAL context.");
		}
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);

		// TODO: move it to camera
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);

		// load the default sounds
		Sound.LoadDefault(new File("res/Sound"));

		// load default movie textures
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary("opencv_ffmpeg400_64");

		// Camera
		GameObject cameraObject = new GameObject();
		Camera.MAIN = cameraObject.AddComponent(new Camera());
		cameraObject.stay_on_stage = true;

	}

	@Override
	public void run() {
		init();

		running = true;
		finishInit = true;
		/*
		 * long lastTime = System.nanoTime(); double ns = 1000000000.0 / 60.0; double
		 * delta = 0.0; int updates = 0; int frames = 0; long timer =
		 * System.currentTimeMillis(); while (running) { long now = System.nanoTime();
		 * delta += (now - lastTime) / ns; lastTime = now; if (delta >= 1.0) { update();
		 * updates++; delta--; } render(); frames++; if (System.currentTimeMillis() -
		 * timer > 1000) { timer += 1000;
		 * 
		 * System.out.println(frames + "fps");
		 * 
		 * updates = 0; frames = 0; } if (glfwWindowShouldClose(window) == true) {
		 * running = false; } }
		 */

		long originalTime = System.currentTimeMillis();
		long accumulator = 0;

		while (running) {
			render();
			accumulator++;
			if (System.currentTimeMillis() - originalTime >= 1000) {
				originalTime = System.currentTimeMillis();
				fps = accumulator;
				accumulator = 0;
			}
			if (glfwWindowShouldClose(window) == true) {
				System.out.println("Application should Close");
				running = false;
			}
		}
		System.out.println("Application is Closing");

		// TODO: remove all the textures
		glfwDestroyWindow(window);
		glfwTerminate();

		// remove the sound
		Sound.cleanUp();
		// remove the sound device
		ALC10.alcCloseDevice(ALdevice);

		System.exit(0);
	}

	private void render() {
		glfwPollEvents(); // Inputs

		synchronized (Main.lock) {
			// Start all gameObjects (Render Loop)
			for (int i = 0; i < Component.toStartComponent.size(); i++) {
				if (Component.toStartComponent.get(i) != null) {
					Component.toStartComponent.get(i).StartRender();
				}
			}
			Component.toStartComponent.clear();

			// Update all gameObjects (Render Loop)
			for (int i = 0; i < UpdatableObject.AllUpdatableObject.size(); i++) {
				UpdatableObject updatableObject = UpdatableObject.AllUpdatableObject.get(i);
				if (updatableObject instanceof GameObject) {
					((GameObject) updatableObject).UpdateRender();
				}
			}
		}

		glFlush();

		synchronized (Main.lock) {
			NewRender();
		}

		// check error

		int error = glGetError();
		if (error != GL_NO_ERROR) {
			System.out.println(error);
			GLUtil.setupDebugMessageCallback();
		}

		glfwSwapBuffers(window);
	}

	private void NewRender() {
		// set the background color to white for rendering mainFrame
		GLCLEAR(new Vector4f(1f, 0.8f, 0.8f, 1));

		LoadFBO(mainFrameBuffer.FrameBufferID);

		RenderFBO(mainFrameBuffer.FrameBufferID);

		// set the background color to black before all other rendering, which is used
		// for effect
		GLCLEAR(new Vector4f(0, 0, 0, 0));

		LoadFBO(bloomFrameBuffer.FrameBufferID);
		RenderFBO(bloomFrameBuffer.FrameBufferID);

		LoadFBO(rippleDistortion.FrameBufferID);
		RenderFBO(rippleDistortion.FrameBufferID);

		// Starting to make the effect work

		// BluR
		// Blur horizontally
		LoadFBO_R(firstBlur.FrameBufferID);

		fullScreenRender(Shader.getShader("HBlur"), bloomFrameBuffer.colorTextureID, 0);

		// Blur vertically
		LoadFBO_R(glowFrameBuffer.FrameBufferID);

		fullScreenRender(Shader.getShader("VBlur"), firstBlur.colorTextureID, 0);

		RenderFBO(glowFrameBuffer.FrameBufferID);

		// Mix the final product with the main frame buffer

		LoadFBO(postBloomShader.FrameBufferID);
		fullScreenRender(Shader.getShader("Bloom"), mainFrameBuffer.colorTextureID, glowFrameBuffer.colorTextureID);

		// The following is the main Rendering
		LoadFBO(0);

		// Added ripple distortion into the postbloom buffer
		fullScreenRender(Shader.getShader("Ripple"), postBloomShader.FrameBufferID, rippleDistortion.colorTextureID);

		// Render the mainframebuffer2 stuff, which is added after all postprocessing
		RenderFBO(mainFrameBuffer2.FrameBufferID);

		// Font rendering
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		FontRenderer.render();

		glDisable(GL_BLEND);
	}

	private void LoadFBO(int FrameBufferID) {
		glViewport(0, 0, Main.getWidth(), Main.getHeight());
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FrameBufferID);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	}

	private void LoadFBO_R(int FrameBufferID) {
		glViewport(0, 0, Main.getHeight(), Main.getWidth());
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, FrameBufferID);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	private void RenderFBO(int FrameBufferID) {
		for (int i = 0; i < SpriteRenderer.allSpriteRenderer.size(); i++) {
			SpriteRenderer.allSpriteRenderer.get(i).render(FrameBufferID);
		}
		InstancedRenderer.Render(FrameBufferID);
	}

	private static void fullScreenRender(Shader shader, int textureID, int textureID2) {
		GL11.glDisable(GL11.GL_BLEND);

		glActiveTexture(GL_TEXTURE1);
		GL11.glBindTexture(GL_TEXTURE_2D, textureID);

		glActiveTexture(GL_TEXTURE2);
		GL11.glBindTexture(GL_TEXTURE_2D, textureID2);

		shader.enable();

		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(new Vector3f(0, 0, -10), 0, new Vector2f(1f, 1f)));
		shader.setUniformMat4f("pr_matrix", new Matrix4f().ortho(0, 1, 1, 0, -10, 10));
		SpriteRenderer.mesh_FULSCREEN.render();

		shader.disable();

		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public static void setWindowSize(Vector2i windowSize) {
		glfwSetWindowSize(window, windowSize.x, windowSize.y);
		Main.setWidth(windowSize.x);
		Main.setHeight(windowSize.y);
		System.out.println("Setting Window Size to x: " + windowSize.x + ",y :" + windowSize.y);
	}

	private static void GLCLEAR(Vector4f backGroundColor) {
		glClearColor(backGroundColor.x, backGroundColor.y, backGroundColor.z, backGroundColor.w);
	}

	/**
	 * get the screen position from 0 to 1 of X
	 */
	public static float getScreenPosition_X(float positionX) {
		return positionX / Main.getWidth();
	}

	/**
	 * get the screen position from 0 to 1 of Y
	 */
	public static float getScreenPosition_Y(float positionY) {
		return positionY / Main.getHeight();
	}
}
