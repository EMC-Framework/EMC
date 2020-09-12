package me.deftware.client.framework.render.batching;

import me.deftware.client.framework.gui.GuiScreen;
import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * new RenderStackImpl()
 * optional glLineWidth
 * .begin().setupMatrix()
 * optional .glColor(color).
 * call to draw functions
 * .end();
 *
 * @author Deftware
 */
@SuppressWarnings("unchecked")
public abstract class RenderStack<T> {
	public static boolean inCustomMatrix = false;
	public static final CopyOnWriteArrayList<Runnable> scaleChangeCallback = new CopyOnWriteArrayList<>();

	public static float getScale() {
		return (float) SettingsMap.getValue(SettingsMap.MapKeys.EMC_SETTINGS, "RENDER_SCALE", 1.0F);
	}

	public static void setScale(float scale) {
		SettingsMap.update(SettingsMap.MapKeys.EMC_SETTINGS, "RENDER_SCALE", scale);
		scaleChangeCallback.forEach(Runnable::run);
	}

	protected boolean customMatrix = true;
	protected boolean locked = false;
	protected boolean running = false;
	protected float red = 1.0F;
	protected float green = 1.0F;
	protected float blue = 1.0F;
	protected float alpha = 1.0F;
	protected float lineWidth = 2.0F;

	public T setupMatrix() {
		GL11.glPushMatrix();
		if (customMatrix) reloadCustomMatrix();
		// Setup gl
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(lineWidth);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		return (T) this;
	}

	public T glLineWidth(float width) {
		this.lineWidth = width;
		return (T) this;
	}

	public T glColor(Color color) {
		return glColor(color, color.getAlpha());
	}

	public T glOverrideMatrix(boolean flag) {
		this.customMatrix = flag;
		return (T) this;
	}

	public T glColor(Color color, float alpha) {
		this.red = color.getRed() / 255.0F;
		this.green = color.getGreen() / 255.0F;
		this.blue = color.getBlue() / 255.0F;
		this.alpha = alpha / 255.0F;
		GL11.glColor4f(this.red, this.green, this.blue, this.alpha);
		return (T) this;
	}

	public abstract T begin();

	public T begin(int mode) {
		GL11.glBegin(mode);
		running = true;
		GL11.glColor4f(this.red, this.green, this.blue, this.alpha);
		return (T) this;
	}

	public void end() {
		if (running) {
			running = false;
			GL11.glEnd();
		}
		if (!locked) {
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
			if (customMatrix) reloadMinecraftMatrix();
		}
	}

	/**
	 * Creates a 1 to 1 pixel matrix
	 */
	public static void reloadCustomMatrix() {
		inCustomMatrix = true;
		// Change matrix
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0, GuiScreen.getDisplayWidth(), GuiScreen.getDisplayHeight(), 0.0, 1000.0, 3000.0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void reloadMinecraftMatrix() {
		inCustomMatrix = false;
		// Revert back to Minecraft
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0, Minecraft.getInstance().mainWindow.getFramebufferWidth() / Minecraft.getInstance().mainWindow.getGuiScaleFactor(), Minecraft.getInstance().mainWindow.getFramebufferHeight() / Minecraft.getInstance().mainWindow.getGuiScaleFactor(), 0.0, 1000.0, 3000.0);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
	}

	public static boolean isInCustomMatrix() {
		return RenderStack.inCustomMatrix;
	}

	public void setCustomMatrix(final boolean customMatrix) {
		this.customMatrix = customMatrix;
	}

	public void setLocked(final boolean locked) {
		this.locked = locked;
	}

	public void setRunning(final boolean running) {
		this.running = running;
	}
}
