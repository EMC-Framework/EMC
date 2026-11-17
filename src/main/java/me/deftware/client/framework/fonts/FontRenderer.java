package me.deftware.client.framework.fonts;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * @author Deftware
 */
public class FontRenderer {

	private static int ensureAlpha(int color) {
		return (color & 0xFF000000) == 0 ? (color | 0xFF000000) : color;
	}

	public static void drawString(GLX context, Message text, int x, int y, int color) {
		drawText(context, text, x, y, ensureAlpha(color), false);
	}

	public static void drawCenteredString(GLX context, Message text, int x, int y, int color) {
		drawText(context, text, x - getTextRenderer().width((Component) text) / 2, y, ensureAlpha(color), true);
	}

	public static void drawStringWithShadow(GLX context, Message text, int x, int y, int color) {
		drawText(context, text, x, y, ensureAlpha(color), true);
	}

	private static void drawText(GLX context, Message text, int x, int y, int argb, boolean shadow) {
		if (context == null || context.getContext() == null) {
			return;
		}
		GuiGraphicsExtractor gfx = context.getContext();
		if (!context.isIn3D()) {
			gfx.text(getTextRenderer(), (Component) text, x, y, argb, shadow);
			return;
		}
		Matrix4f viewProj = context.getViewProjection3d();
		if (viewProj == null) {
			return;
		}
		Matrix4f mvp = new Matrix4f(viewProj).mul(context.getModel());
		Vector4f origin = mvp.transform(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
		if (origin.w < 0.05f) {
			return;
		}
		float guiW = gfx.guiWidth();
		float guiH = gfx.guiHeight();
		float ox = (origin.x / origin.w * 0.5f + 0.5f) * guiW;
		float oy = (0.5f - origin.y / origin.w * 0.5f) * guiH;
		Vector4f unitX = mvp.transform(new Vector4f(10.0f, 0.0f, 0.0f, 1.0f));
		float s = 1.0f;
		if (unitX.w >= 0.05f) {
			float ux = (unitX.x / unitX.w * 0.5f + 0.5f) * guiW;
			float uy = (0.5f - unitX.y / unitX.w * 0.5f) * guiH;
			s = (float) Math.hypot(ux - ox, uy - oy) / 10.0f;
		}
		if (s < 0.05f || !Float.isFinite(s)) {
			return;
		}
		s = Math.min(4.0f, s);
		gfx.pose().pushMatrix();
		gfx.pose().translate(ox, oy);
		gfx.pose().scale(s, s);
		gfx.text(getTextRenderer(), (Component) text, x, y, argb, shadow);
		gfx.pose().popMatrix();
	}

	public static int getFontHeight() {
		return Minecraft.getInstance().font.lineHeight;
	}

	public static int getStringWidth(Message string) {
		return Minecraft.getInstance().font.width((Component) string);
	}

	public static int getStringWidth(String string) {
		return Minecraft.getInstance().font.width(string);
	}

	private static Font getTextRenderer() {
		return Minecraft.getInstance().font;
	}

}
