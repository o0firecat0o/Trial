package engine.component.graphic;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.utils.BufferUtils;
import engine.utils.ShaderUtils;

public class Shader {

	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;

	private boolean enabled = false;

	public final int ID;
	public boolean isUIShader = false;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	// the static dictionary that contains all the NAME and ID of the texture to
	// prevent double instantiation
	private static Map<String, Shader> NAME_SHADER_DICTIONARY = new HashMap<String, Shader>();

	// ArrayList of shaders that will be rendered this frame, used for camera
	public static ArrayList<Shader> rendered_shader = new ArrayList<>();

	public static Object[] returnAllShaders() {
		return NAME_SHADER_DICTIONARY.values().toArray();
	}

	public static void loadDefault() {
		Shader.createShader("shaders/default.vert", "shaders/default.frag", "DEFAULT")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1);
		Shader.createShader("shaders/default.vert", "shaders/default.frag", "UI")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).isUIShader();
		Shader.createShader("shaders/BlurHorizontal.vert", "shaders/BlurFragmentShader.frag", "HBlur")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).isUIShader();
		Shader.createShader("shaders/BlurVertical.vert", "shaders/BlurFragmentShader.frag", "VBlur")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).isUIShader();
		Shader.createShader("shaders/default.vert", "shaders/bloom.frag", "Bloom")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.isUIShader();
		Shader.createShader("shaders/default.vert", "shaders/RippleDistortion.frag", "Ripple")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.isUIShader();
		Shader.createShader("shaders/default.vert", "shaders/Shield.frag", "Shield")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.setUniform1i("tex3", 3).setUniform1i("tex4", 4);
		Shader.createShader("shaders/default.vert", "shaders/Lazer.frag", "Lazer")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.setUniform1i("tex3", 3).setUniform1i("tex4", 4).setUniform1i("tex5", 5).setUniform1i("tex6", 6)
				.setUniform1i("tex7", 7);
		Shader.createShader("shaders/default.vert", "shaders/LazerCurved.frag", "LazerCurved")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix);
		Shader.createShader("shaders/defaultParticle.vert", "shaders/defaultParticle.frag", "DefaultParticle")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1);
		Shader.createShader("shaders/default.vert", "shaders/CircularMeter.frag", "CircularMeter")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix);
		Shader.createShader("shaders/default.vert", "shaders/Trail.frag", "Trail").setUniform1i("tex", 1)
				.setUniform1i("tex2", 2).isUIShader();
	}

	public static Shader createShader(String vertex, String fragment, String Name) {
		if (NAME_SHADER_DICTIONARY.containsKey(Name)) {
			System.out.println("Shader already created, it this intended?");
			return NAME_SHADER_DICTIONARY.get(Name);
		} else {
			Shader shader = new Shader(vertex, fragment);
			NAME_SHADER_DICTIONARY.put(Name, shader);
			return shader;
		}
	}

	public static boolean hasShader(String Name) {
		if (NAME_SHADER_DICTIONARY.containsKey(Name)) {
			return true;
		} else {
			return false;
		}
	}

	public static Shader getShader(String Name) throws RuntimeException {
		Shader shader = NAME_SHADER_DICTIONARY.get(Name);
		if (!rendered_shader.contains(shader)) {
			rendered_shader.add(shader);
		}
		return shader;
	}

	public static int getID(String Name) {
		return NAME_SHADER_DICTIONARY.get(Name).ID;
	}

	public Shader isUIShader() {
		this.isUIShader = true;
		return this;
	}

	private Shader(String vertex, String fragment) {
		ID = ShaderUtils.load(vertex, fragment);
	}

	public int getUniform(String name) {
		if (locationCache.containsKey(name)) {
			return locationCache.get(name);
		}
		int result = glGetUniformLocation(ID, name);
		if (result == -1) {
			System.err.println("Could not find uniform variable '" + name
					+ "'! @shader/getUniform, make sure the uniform variables is used in the shaders");
		} else {
			locationCache.put(name, result);
		}
		return result;
	}

	public Shader setUniform1i(String name, int value) {
		if (!enabled)
			enable();
		glUniform1i(getUniform(name), value);
		return this;
	}

	public Shader setUniform1f(String name, float value) {
		if (!enabled)
			enable();
		glUniform1f(getUniform(name), value);
		return this;
	}

	public Shader setUniform2f(String name, float x, float y) {
		if (!enabled)
			enable();
		glUniform2f(getUniform(name), x, y);
		return this;
	}

	public Shader setUniform3f(String name, Vector3f vector3) {
		if (!enabled)
			enable();
		glUniform3f(getUniform(name), vector3.x, vector3.y, vector3.z);
		return this;
	}

	public Shader setUniformMat4f(String name, Matrix4f matrix) {
		if (!enabled)
			enable();
		glUniformMatrix4fv(getUniform(name), false,
				BufferUtils.createFloatBuffer(new float[] { matrix.m00(), matrix.m01(), matrix.m02(), matrix.m03(),
						matrix.m10(), matrix.m11(), matrix.m12(), matrix.m13(), matrix.m20(), matrix.m21(),
						matrix.m22(), matrix.m23(), matrix.m30(), matrix.m31(), matrix.m32(), matrix.m33() }));
		return this;
	}

	public void enable() {
		glUseProgram(ID);
		enabled = true;
	}

	public void disable() {
		glUseProgram(0);
		enabled = false;
	}
}
