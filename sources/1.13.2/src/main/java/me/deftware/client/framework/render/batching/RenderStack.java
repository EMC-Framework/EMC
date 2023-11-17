package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;
import me.deftware.mixin.mixins.render.BufferBuilderAccessor;

/**
 * new RenderStackImpl()

 * optional glLineWidth
<<<<<<< HEAD

 * .begin().setupMatrix()

=======
 * .begin()
>>>>>>> fef22fd0... Add GLX
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

	protected boolean scaled = true;

	protected float red = 1f, green = 1f, blue = 1f, alpha = 1f, lineWidth = 2f;
	protected Color lastColor = Color.white;

	protected BufferBuilder builder = Tessellator.getInstance().getBuffer();
	protected int mode = -1;

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
		GlStateManager.lineWidth(lineWidth);
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
		GlStateManager.lineWidth(lineWidth);
		builder.begin(this.mode = mode, getFormat());
		return (T) this;
	}

	public void end() {
		drawBuffer();
	}

	public boolean isBuilding() {
		return ((BufferBuilderAccessor) builder).isBuilding();
	}

	protected void drawBuffer() {
		Tessellator.getInstance().draw();
	}

	public static void blend() {
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
	}

	public static void noBlend() {
		GlStateManager.disableBlend();
	}

	public static void setupGl() {
		blend();
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepthTest();
		GlStateManager.depthMask(false);
	}

	public static void restoreGl() {
		noBlend();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepthTest();
	}

	@Override
	public void next() {
		builder.endVertex();
	}

	@Override
	public VertexConstructor color(float r, float g, float b, float alpha) {
		builder.color(r, g, b, alpha);
		return this;
	}

	@Override
	public VertexConstructor texture(float u, float v) {
		builder.tex(u, v);
		return this;
	}

	@Override
	public VertexConstructor vertex(double x, double y, double z) {
		builder.pos(x,y, z).color(red, green, blue, alpha);
		return this;
	}

	protected VertexFormat getFormat() {
		return DefaultVertexFormats.POSITION_COLOR;
	}

	private static boolean inCustomMatrix = false;

	/**
	 * Creates a 1 to 1 pixel matrix
	 */
	public static void reloadCustomMatrix() {
		if (inCustomMatrix)
			throw new IllegalStateException("Already in custom matrix!");
		inCustomMatrix = true;
		// Change matrix
		MainWindow window = Minecraft.getInstance().mainWindow;
		setMatrix(
				(float) window.getWidth(),
				(float) window.getHeight()
		);
	}

	public static void reloadMinecraftMatrix() {
		if (!inCustomMatrix)
			throw new IllegalStateException("Already in Minecraft matrix!");
		inCustomMatrix = false;
		MainWindow window = Minecraft.getInstance().mainWindow;
		setMatrix(
				(float) (window.getFramebufferWidth() / window.getGuiScaleFactor()),
				(float) (window.getFramebufferHeight() / window.getGuiScaleFactor())
		);
	}

	protected static void setMatrix(float width, float height) {
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
		GlStateManager.clear(256);
	}

	public static boolean isInCustomMatrix() {
		return RenderStack.inCustomMatrix;
	}

	public boolean isScaled() {
		return scaled;
	}

}
