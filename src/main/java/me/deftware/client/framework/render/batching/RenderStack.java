package me.deftware.client.framework.render.batching;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import lombok.Getter;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * new RenderStackImpl()
 * optional glLineWidth
 * .begin()
 * optional .glColor(color).
 * call to draw functions
 * .end();
 *
 * @author Deftware
 */
@SuppressWarnings("unchecked")
public abstract class RenderStack<T> implements VertexConstructor {

	public static float scale = 1;
	public static final CopyOnWriteArrayList<Runnable> scaleChangeCallback = new CopyOnWriteArrayList<>();

	public static float getScale() {
		return scale;
	}

	public static void setScale(float scale) {
		if (scale < 0.5f)
			scale = 0.5f;
		if (scale > 4)
			scale = 4;
		Bootstrap.EMCSettings.putPrimitive("RENDER_SCALE", RenderStack.scale = scale);
		scaleChangeCallback.forEach(Runnable::run);
	}

	@Getter
	protected boolean scaled = true;

	protected float red = 1f, green = 1f, blue = 1f, alpha = 1f, lineWidth = 2f;
	protected Color lastColor = Color.white;

	protected BufferBuilder builder = null;
	private int mode = -1;

	private GLX context;

	public T setScaled(boolean scaling) {
		this.scaled = scaling;
		return (T) this;
	}

	public T push() {
		context.push();
		return (T) this;
	}

	public T pop() {
		context.pop();
		return (T) this;
	}

	public T lineWidth(float width) {
		this.lineWidth = width;
		RenderSystem.lineWidth(lineWidth);
		return (T) this;
	}

	public T glColor(int rgb, float alpha) {
		this.red = ((rgb >> 16) & 0xFF) / 255f;
		this.green = ((rgb >> 8) & 0xFF) / 255f;
		this.blue = (rgb & 0xFF) / 255f;
		this.alpha = alpha / 255f;
		return (T) this;
	}

	public T glColor(Color color) {
		return glColor(color, color.getAlpha());
	}

	public T glColor(Color color, float alpha) {
		this.red = color.getRed() / 255.0F;
		this.green = color.getGreen() / 255.0F;
		this.blue = color.getBlue() / 255.0F;
		this.alpha = alpha / 255.0F;
		lastColor = color;
		return (T) this;
	}

	public abstract T begin(GLX context);

	public T begin(GLX context, int mode) {
		this.context = context;
		this.mode = mode;
		setShader();
		RenderSystem.lineWidth(lineWidth);
		setBuilder(translate(mode), getFormat());
		return (T) this;
	}


	protected void setBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat) {
		builder = Tessellator.getInstance().begin(drawMode, vertexFormat);
	}

	public void end() {
		drawBuffer();
	}

	public boolean isBuilding() {
		return builder != null;
	}

	protected void drawBuffer() {
		var shader = RenderSystem.getShader();
		if (shader != null && shader.lineWidth != null) {
			shader.lineWidth.set(RenderSystem.getShaderLineWidth());
		}
		var result = builder.endNullable();
		if (result != null) {
			BufferRenderer.drawWithGlobalProgram(result);
		}
		builder = null;
	}

	public static void noBlend() {
		RenderSystem.disableBlend();
	}

	public static void blend() {
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.enableBlend();
	}

	public static void setupGl() {
		blend();
		// RenderSystem.disableTexture();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
	}

	public static void restoreGl() {
		noBlend();
		RenderSystem.depthMask(true);
		// RenderSystem.enableTexture();
		RenderSystem.enableDepthTest();
	}

	public static VertexFormat.DrawMode translate(int mode) {
		return switch (mode) {
			case GL11.GL_QUADS -> VertexFormat.DrawMode.QUADS;
			case GL11.GL_LINE_STRIP -> VertexFormat.DrawMode.DEBUG_LINE_STRIP;
			case GL11.GL_LINES ->
				// Not sure why DEBUG_LINES behaves differently from LINES
				// LINES does not work at all.
					VertexFormat.DrawMode.DEBUG_LINES;
			case GL11.GL_TRIANGLE_FAN -> VertexFormat.DrawMode.TRIANGLE_FAN;
			case GL11.GL_TRIANGLE_STRIP -> VertexFormat.DrawMode.TRIANGLE_STRIP;
			case GL11.GL_TRIANGLES -> VertexFormat.DrawMode.TRIANGLES;
			default -> VertexFormat.DrawMode.QUADS;
		};
	}

	protected Matrix4f getModel() {
		return context.getModel();
	}

	@Override
	public void next() {
		// builder.next();
	}

	@Override
	public VertexConstructor color(float r, float g, float b, float alpha) {
		builder.color(r, g, b, alpha);
		return this;
	}

	@Override
	public VertexConstructor texture(float u, float v) {
		builder.texture(u, v);
		return this;
	}

	@Override
	public VertexConstructor vertex(double x, double y, double z) {
		builder.vertex(getModel(), (float) x, (float) y, (float) z).color(red, green, blue, alpha);
		return this;
	}

	@Override
	public VertexConstructor normal(float x, float y, float z) {
		builder.normal(x, y, z);
		return this;
	}

	protected VertexFormat getFormat() {
		return VertexFormats.POSITION_COLOR;
	}

	protected void setShader() {
		RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
	}

	@Getter
	private static boolean inCustomMatrix = false;

	/**
	 * Creates a 1 to 1 pixel matrix
	 */
	public static void reloadCustomMatrix() {
		if (inCustomMatrix)
			throw new IllegalStateException("Already in custom matrix!");
		inCustomMatrix = true;
		// Change matrix
		Window window = MinecraftClient.getInstance().getWindow();
		setMatrix(
				(float) window.getWidth(),
				(float) window.getHeight()
		);
	}

	public static void reloadMinecraftMatrix() {
		if (!inCustomMatrix)
			throw new IllegalStateException("Already in Minecraft matrix!");
		inCustomMatrix = false;
		Window window = MinecraftClient.getInstance().getWindow();
		setMatrix(
				(float) (window.getFramebufferWidth() / window.getScaleFactor()),
				(float) (window.getFramebufferHeight() / window.getScaleFactor())
		);
	}

	protected static void setMatrix(float width, float height) {
		RenderSystem.clear(GlConst.GL_DEPTH_BUFFER_BIT);
		Matrix4f matrix4f = new Matrix4f().setOrtho(0f, width, height, 0f, 1000f, 21000f);
		RenderSystem.setProjectionMatrix(matrix4f, VertexSorter.BY_Z);
		var matrixStack = RenderSystem.getModelViewStack();
		matrixStack.identity();
		matrixStack.translate(0f, 0f, -11000f);
	}

}
