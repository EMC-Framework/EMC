package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.client.framework.render.shader.Shader;
import net.minecraft.util.math.Matrix4f;
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

	public QuadRenderStack drawRect(double x, double y, double xx, double yy, boolean scaling) {
		return drawRect(x, y, xx, yy, scaling, null);
	}

	public QuadRenderStack drawRect(double x, double y, double xx, double yy, boolean scaling, Shader shader) {
		return drawRect((float) x, (float) y, (float) xx, (float) yy, scaling, shader);
	}

	public QuadRenderStack drawRect(float x, float y, float xx, float yy, boolean scaling) {
		return drawRect(x, y, xx, yy, scaling, null);
	}

	public QuadRenderStack drawRect(float x, float y, float xx, float yy, boolean scaling, Shader shader) {
		// Apply scaling
		if (scaling) {
			x *= getScale();
			y *= getScale();
			xx *= getScale();
			yy *= getScale();
		}
		if (shader != null)
			setupUniforms(x, y, xx - x, yy - y, shader);
		// Draw
		Matrix4f model = GLX.INSTANCE.getModel();
		builder.vertex(model, xx, y, 0).color(red, green, blue, alpha).next();
		builder.vertex(model, x, y, 0).color(red, green, blue, alpha).next();
		builder.vertex(model, x, yy, 0).color(red, green, blue, alpha).next();
		builder.vertex(model, xx, yy, 0).color(red, green, blue, alpha).next();
		return this;
	}
	
	public void setupUniforms(float x, float y, float width, float height, Shader shader) {
		int resolution = GL20.glGetUniformLocation(shader.getProgram(), "resolution"),
			coordinates = GL20.glGetUniformLocation(shader.getProgram(), "coordinates");
		GL20.glUniform4f(resolution, width, height, GuiScreen.getDisplayWidth(), GuiScreen.getDisplayHeight());
		GL20.glUniform2f(coordinates, x, y);
		running = true;
	}

}
