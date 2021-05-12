package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.render.shader.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * @author Deftware
 */
public class QuadRenderStack extends RenderStack<QuadRenderStack> {

	@Override
	public QuadRenderStack begin() {
		return begin(GL11.GL_QUADS);
	}

	public QuadRenderStack drawRect(double x, double y, double xx, double yy) {
		return drawRect(x, y, xx, yy, null);
	}

	public QuadRenderStack drawRect(double x, double y, double xx, double yy, Shader shader) {
		return drawRect((float) x, (float) y, (float) xx, (float) yy, shader);
	}

	public QuadRenderStack drawRect(float x, float y, float xx, float yy) {
		return drawRect(x, y, xx, yy, null);
	}

	public QuadRenderStack drawRect(float x, float y, float xx, float yy, Shader shader) {
		// Apply scaling
		if (scaled) {
			x *= getScale();
			y *= getScale();
			xx *= getScale();
			yy *= getScale();
		}
		if (shader != null)
			setupUniforms(x, y, xx - x, yy - y, shader);
		// Draw
		vertex(xx, y, 0).endVertex();
		vertex(x, y, 0).endVertex();
		vertex(x, yy, 0).endVertex();
		vertex(xx, yy, 0).endVertex();
		return this;
	}
	
	public void setupUniforms(float x, float y, float width, float height, Shader shader) {
		int resolution = GL20.glGetUniformLocation(shader.getProgram(), "resolution"),
			coordinates = GL20.glGetUniformLocation(shader.getProgram(), "coordinates");
		GL20.glUniform4f(resolution, width, height, GuiScreen.getDisplayWidth(), GuiScreen.getDisplayHeight());
		GL20.glUniform2f(coordinates, x, y);
	}

}
