package engine.font;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import engine.component.graphic.Shader;
import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.Texture;
import engine.main.Main;
import engine.math.Maths;
import engine.object.Component;
import engine.object.UIObject;

public class Font extends Component {

	public static Matrix4f pr_matrix = new Matrix4f().ortho(0, Main.getWidth(), 0, Main.getHeight(), -10, 10);

	public String textureName;

	public int Text_Length = 15; // The length before go to the next line

	float FontSize = 1;

	private String fontString;

	private int number_of_lines;
	private ArrayList<Character> characters = new ArrayList<>();

	private Vector2f scale = new Vector2f(1, 1);

	@Override
	protected void Update() {

	}

	@Override
	protected void Start() {

	}

	public void Render() {
		glActiveTexture(GL_TEXTURE1);
		GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture("FONT"));
		glActiveTexture(GL_TEXTURE2);
		GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture(textureName));

		Shader shader;
		if (gameObject instanceof UIObject) {
			shader = Shader.getShader("UIFONT");
		} else {
			shader = Shader.getShader("FONT");
		}
		shader.enable();
		shader.setUniformMat4f("ml_matrix",
				Maths.createTransformationMatrix(new Vector3f(0, 0, 0.1f).add(gameObject.transform.position),
						gameObject.transform.rotation, new Vector2f(scale.x * FontSize, scale.y * FontSize)))
				.setUniform1i("x_size", Text_Length).setUniform1i("y_size", number_of_lines);

		SpriteRenderer.mesh_NORAML.render();
		shader.disable();
		glBindTexture(GL_TEXTURE_2D, 0);

		glActiveTexture(GL_TEXTURE1);
	}

	// TODO: add position offset
	public Font(String fontstring, int Text_Length) {
		this.fontString = fontstring;
		this.Text_Length = Text_Length;
		textureName = Texture.getUnusedTextureName();
	}

	@Override
	public void StartRender() {
		CreateFontArray();

		Texture.createTexture(textureName, CharToIntArray(characters), this.Text_Length, number_of_lines);
		FontRenderer.allFontRenderer.add(this);
	}

	// flag
	// the flag is needed because create texture function has to be run in the
	// render thread rather than the logic thread
	boolean TextChanged = false;

	// change the text of the font
	public void setText(String text) {
		// if the setted text is equal to the original
		// dont make any changes
		if (text == fontString) {
			return;
		}
		fontString = text;
		CreateFontArray();
		TextChanged = true;
	}

	public String getText() {
		return fontString;
	}

	@Override
	public void UpdateRender() {
		if (TextChanged == true) {

			// Update new texture
			Texture.createTexture(textureName, CharToIntArray(characters), this.Text_Length, number_of_lines);
			TextChanged = false;
		}
	}

	// public void UpdateMesh() {
	// // float SIZE_X = (Text_Length) * 100 / 2;
	// // float SIZE_Y = number_of_lines * 100 / 2;
	// // vertices = new float[] { -SIZE_X, -SIZE_Y, 0f, -SIZE_X, SIZE_Y, 0f,
	// // SIZE_X, SIZE_Y, 0f, SIZE_X, -SIZE_Y, 0f };
	// // indices = new byte[] { 0, 1, 2, 2, 3, 0 };
	// // tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };
	// //
	// // if (mesh == null) {
	// // // if the mesh has not been generated yet
	// // mesh = new VertexArray(vertices, indices, tcs);
	// // } else {
	// // mesh.bind();
	// // glBindBuffer(GL_ARRAY_BUFFER, mesh.vbo);
	// // glBufferData(GL_ARRAY_BUFFER,
	// // BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
	// // glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0,
	// // 0);
	// // glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);
	// // mesh.unbind();
	// // }
	// }

	public Font setFontSize(float fontSize) {
		this.FontSize = fontSize;
		return this;
	}

	// generate a string array of characters from fontstring
	public void CreateFontArray() {

		// Cut the words
		String[] words = fontString.split("\\s+");

		// Increase Text_Length if the text is too long;
		for (String word : words) {
			if (word.length() > Text_Length) {
				Text_Length = word.length();
			}
		}

		// start building the array
		characters = new ArrayList<>();
		number_of_lines = 1;

		int currentLineLength = 0;
		int currentWords = 0;
		while (currentWords < words.length) {
			// start a new line
			if (words[currentWords].equals("/n")) {
				for (int i = 0; i < Text_Length - currentLineLength; i++) {
					characters.add(' ');
				}
				currentLineLength = 0;
				currentWords += 1;
				number_of_lines++;
			}
			// normal words
			// if the next word and the current line exceed the line length
			if (words[currentWords].length() + currentLineLength > Text_Length) {
				// Add to the end
				for (int i = 0; i < Text_Length - currentLineLength; i++) {
					characters.add(' ');
				}
				currentLineLength = 0;
				number_of_lines++;
			}
			for (int i = 0; i < words[currentWords].length(); i++) {
				characters.add(words[currentWords].charAt(i));
			}
			currentLineLength += words[currentWords].length() + 1;
			currentWords += 1;
			// if it is not the last letter
			if (currentLineLength != Text_Length + 1) {
				characters.add(' ');
			}
		}
		// if it is only 1 line text
		if (currentLineLength < Text_Length && number_of_lines == 1) {
			Text_Length = currentLineLength;
		}
		while (characters.size() < number_of_lines * Text_Length) {
			characters.add(' ');
		}

		// set the scale for Rendering
		scale.x = Text_Length;
		scale.y = number_of_lines;
	}

	// TODO: add destroy function?
	@Override
	public void Destroy() {
		// clean up the glsl memory

		// GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		// GL15.glDeleteBuffers(mesh.vbo);
		//
		// GL30.glBindVertexArray(0);
		// GL30.glDeleteVertexArrays(mesh.vao);

		// TODO: the destroy function is here,
		// However this is not the logic thread, has to add to update thread
		// Delete the original texture
		// glDeleteTextures(texture);

		FontRenderer.allFontRenderer.remove(this);

		super.Destroy();
	}

	public static int[] CharToIntArray(ArrayList<Character> characters) {
		int[] returnIntArray = new int[characters.size()];
		for (int i = 0; i < characters.size(); i++) {
			returnIntArray[i] = CharToInt(characters.get(i));
		}
		return returnIntArray;
	}

	public static int CharToInt(Character character) {
		switch (character) {
		case ' ':
			return 0;
		case '!':
			return 1;
		case '"':
			return 2;
		case '#':
			return 3;
		case '$':
			return 4;
		case '%':
			return 5;
		case '&':
			return 6;
		case '\'':
			return 7;
		case '(':
			return 8;
		case ')':
			return 9;
		case '*':
			return 10;
		case '+':
			return 11;
		case ',':
			return 12;
		case '-':
			return 13;
		case '.':
			return 14;
		case '/':
			return 15;
		case '0':
			return 16;
		case '1':
			return 17;
		case '2':
			return 18;
		case '3':
			return 19;
		case '4':
			return 20;
		case '5':
			return 21;
		case '6':
			return 22;
		case '7':
			return 23;
		case '8':
			return 24;
		case '9':
			return 25;
		case ':':
			return 26;
		case ';':
			return 27;
		case '<':
			return 28;
		case '=':
			return 29;
		case '>':
			return 30;
		case '?':
			return 31;
		case '@':
			return 32;
		case 'A':
			return 33;
		case 'B':
			return 34;
		case 'C':
			return 35;
		case 'D':
			return 36;
		case 'E':
			return 37;
		case 'F':
			return 38;
		case 'G':
			return 39;
		case 'H':
			return 40;
		case 'I':
			return 41;
		case 'J':
			return 42;
		case 'K':
			return 43;
		case 'L':
			return 44;
		case 'M':
			return 45;
		case 'N':
			return 46;
		case 'O':
			return 47;
		case 'P':
			return 48;
		case 'Q':
			return 49;
		case 'R':
			return 50;
		case 'S':
			return 51;
		case 'T':
			return 52;
		case 'U':
			return 53;
		case 'V':
			return 54;
		case 'W':
			return 55;
		case 'X':
			return 56;
		case 'Y':
			return 57;
		case 'Z':
			return 58;
		case '[':
			return 59;
		case '\\':
			return 60;
		case ']':
			return 61;
		case '^':
			return 62;
		case '_':
			return 63;
		case '`':
			return 64;
		case 'a':
			return 65;
		case 'b':
			return 66;
		case 'c':
			return 67;
		case 'd':
			return 68;
		case 'e':
			return 69;
		case 'f':
			return 70;
		case 'g':
			return 71;
		case 'h':
			return 72;
		case 'i':
			return 73;
		case 'j':
			return 74;
		case 'k':
			return 75;
		case 'l':
			return 76;
		case 'm':
			return 77;
		case 'n':
			return 78;
		case 'o':
			return 79;
		case 'p':
			return 80;
		case 'q':
			return 81;
		case 'r':
			return 82;
		case 's':
			return 83;
		case 't':
			return 84;
		case 'u':
			return 85;
		case 'v':
			return 86;
		case 'w':
			return 87;
		case 'x':
			return 88;
		case 'y':
			return 89;
		case 'z':
			return 90;
		case '{':
			return 91;
		case '|':
			return 92;
		case '}':
			return 93;
		case '~':
			return 94;
		}
		return 95;
	}

}
