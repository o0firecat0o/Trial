package engine.component.graphic;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Texture {

	// the static dictionary that contains all the NAME and ID of the texture to
	// prevent double instantiation
	private final static Map<String, Integer> NAME_ID_DICTIONARY = new HashMap<String, Integer>();

	// create a texture, if texture exist, return the existing texture object
	public static int createTexture(String path, String name) {
		if (NAME_ID_DICTIONARY.containsKey(name)) {
			return NAME_ID_DICTIONARY.get(name);
		} else {
			int texture = load(path);
			NAME_ID_DICTIONARY.put(name, texture);
			return texture;
		}
	}

	public static String getTexture(int ID) {
		for (Map.Entry<String, Integer> entry : NAME_ID_DICTIONARY.entrySet()) {
			if (entry.getValue().equals(ID)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static int getTexture(String name) {
		try {
			return NAME_ID_DICTIONARY.get(name);
		} catch (Exception e) {
			System.out.println("Texture: " + name + " does not exist");
			return 0;
		}
	}

	public static boolean hasTexture(String name) {
		return NAME_ID_DICTIONARY.containsKey(name);
	}

	@Deprecated
	/**
	 * This function is Deprecated because it could not run in the logic loop, it
	 * has to be called in the render loop
	 * 
	 * @param name Name of the texture
	 */
	public static void destroyTexture(String name) {
		int textureID = NAME_ID_DICTIONARY.get(name);
		glDeleteTextures(textureID);
		NAME_ID_DICTIONARY.remove(name);
	}

	private static int load(String path) {
		BufferedImage bImage;
		int width;
		int height;
		int texture;
		try {
			bImage = ImageIO.read(new File(path));
			width = bImage.getWidth();
			height = bImage.getHeight();

			int[] pixels_raw = new int[width * height];
			pixels_raw = bImage.getRGB(0, 0, width, height, null, 0, width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int pixel = pixels_raw[i * width + j];

					pixels.put((byte) ((pixel >> 16) & 0xFF));// RED
					pixels.put((byte) ((pixel >> 8) & 0xFF));// GREEN
					pixels.put((byte) ((pixel) & 0xFF));// BLUE
					pixels.put((byte) ((pixel >> 24) & 0xFF));// ALPHA

				}
			}

			pixels.flip();

			texture = GL11.glGenTextures();

			GL11.glBindTexture(GL_TEXTURE_2D, texture);

			GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

			return texture;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static int createTexture(String name, int[] Array, int width, int height) {

		int texture;

		if (NAME_ID_DICTIONARY.containsKey(name)) {
			texture = NAME_ID_DICTIONARY.get(name);
		} else {
			texture = GL11.glGenTextures();
			NAME_ID_DICTIONARY.put(name, texture);
		}

		GL11.glBindTexture(GL_TEXTURE_2D, texture);

		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				pixels.put((byte) (Array[i * width + j]));// ALPHA
				pixels.put((byte) (Array[i * width + j]));// ALPHA
				pixels.put((byte) (Array[i * width + j]));// ALPHA
				pixels.put((byte) (Array[i * width + j]));// ALPHA
			}
		}

		pixels.flip();

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

		return texture;
	}

	/**
	 * Create texture from a buffered image, bufferedimage could be coming from
	 * native java robot screen capture
	 * 
	 * @param name
	 * @param image The Buffered Image
	 * @return
	 */
	public static int createTexture(String name, BufferedImage image) {

		int texture;

		if (NAME_ID_DICTIONARY.containsKey(name)) {
			texture = NAME_ID_DICTIONARY.get(name);
		} else {
			texture = GL11.glGenTextures();
			NAME_ID_DICTIONARY.put(name, texture);
		}

		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels_raw = new int[width * height];
		pixels_raw = image.getRGB(0, 0, width, height, null, 0, width);

		GL11.glBindTexture(GL_TEXTURE_2D, texture);

		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = pixels_raw[i * width + j];

				pixels.put((byte) ((pixel >> 16) & 0xFF));// RED
				pixels.put((byte) ((pixel >> 8) & 0xFF));// GREEN
				pixels.put((byte) ((pixel) & 0xFF));// BLUE
				pixels.put((byte) ((pixel >> 24) & 0xFF));// ALPHA

			}
		}

		pixels.flip();

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

		return texture;
	}

	/**
	 * Create an empty texture with nothing inside for later use
	 * 
	 * @param name
	 * @return
	 */
	public static int createTexture(String name) {
		int texture;

		if (NAME_ID_DICTIONARY.containsKey(name)) {
			texture = NAME_ID_DICTIONARY.get(name);
		} else {
			texture = GL11.glGenTextures();
			NAME_ID_DICTIONARY.put(name, texture);
		}

		return texture;
	}

	public static void bind(int textureID) {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public static boolean containTexture(String name) {
		if (NAME_ID_DICTIONARY.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}

	static int Counter = 0;

	public static String getUnusedTextureName() {
		Counter++;
		return "S" + Counter;
	}

	public static void LoadDefault(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				LoadDefault(fileEntry);
			} else {
				Texture.createTexture(fileEntry.getPath(),
						fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf('.')));
			}
		}
	}
}
