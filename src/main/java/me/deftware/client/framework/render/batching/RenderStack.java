package me.deftware.client.framework.render.batching;

import com.mojang.blaze3d.platform.GlStateManager;
import lombok.Getter;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.render.gl.GLX;
import me.deftware.mixin.mixins.render.BufferBuilderAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
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

	protected BufferBuilder builder = Tessellator.getInstance().getBuffer();
	protected int mode = -1;

	public T setScaled(boolean scaling) {
		this.scaled = scaling;
		return (T) this;
	}

	public T push() {
		GLX.INSTANCE.push();
		return (T) this;
	}

	public T pop() {
		GLX.INSTANCE.pop();
		return (T) this;
	}

	public T lineWidth(float width) {
		this.lineWidth = width;
		GlStateManager.lineWidth(lineWidth);
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

	public abstract T begin();

	public T begin(int mode) {
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
		GlStateManager.disableTexture();
		GlStateManager.disableDepthTest();
		GlStateManager.depthMask(false);
	}

	public static void restoreGl() {
		noBlend();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture();
		GlStateManager.enableDepthTest();
	}

	@Override
	public void next() {
		builder.next();
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
		builder.vertex(x,y, z).color(red, green, blue, alpha);
		return this;
	}

	protected VertexFormat getFormat() {
		return VertexFormats.POSITION_COLOR;
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
		Window window = MinecraftClient.getInstance().window;
		setMatrix(
				(float) window.getWidth(),
				(float) window.getHeight()
		);
	}

	public static void reloadMinecraftMatrix() {
		if (!inCustomMatrix)
			throw new IllegalStateException("Already in Minecraft matrix!");
		inCustomMatrix = false;
		Window window = MinecraftClient.getInstance().window;
		setMatrix(
				(float) (window.getFramebufferWidth() / window.getScaleFactor()),
				(float) (window.getFramebufferHeight() / window.getScaleFactor())
		);
	}

	protected static void setMatrix(float width, float height) {
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
		GlStateManager.clear(256, MinecraftClient.IS_SYSTEM_MAC);
	}

}
