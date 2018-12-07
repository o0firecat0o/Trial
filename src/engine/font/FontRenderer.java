package engine.font;

import java.util.ArrayList;

import engine.component.graphic.*;

//Maybe remove this?
public class FontRenderer {

	public static ArrayList<Font> allFontRenderer = new ArrayList<>();

	public static void init() {
		Shader.createShader("shaders/font.vert", "shaders/font.frag", "FONT")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2);
		Shader.createShader("shaders/font.vert", "shaders/font.frag", "UIFONT")
				.setUniformMat4f("pr_matrix", SpriteRenderer.pr_matrix).setUniform1i("tex", 1).setUniform1i("tex2", 2)
				.isUIShader();
	}

	public static void render() {
		allFontRenderer.forEach(x -> x.Render());
	}
}
