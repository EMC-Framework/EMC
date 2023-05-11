package me.deftware.client.framework.fonts;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * @author Deftware
 */
public class FontRenderer {

	public static void drawString(GLX context, Message text, int x, int y, int color) {
		context.getContext().drawText(getTextRenderer(), (Text) text, x, y, color, false);
	}

	public static void drawCenteredString(GLX context, Message text, int x, int y, int color) {
		context.getContext().drawTextWithShadow(getTextRenderer(), (Text) text, x - getTextRenderer().getWidth((Text) text) / 2, y, color);
	}

	public static void drawStringWithShadow(GLX context, Message text, int x, int y, int color) {
		context.getContext().drawTextWithShadow(getTextRenderer(), (Text) text, x, y, color);
	}

	public static int getFontHeight() {
		return MinecraftClient.getInstance().textRenderer.fontHeight;
	}

	public static int getStringWidth(Message string) {
		return MinecraftClient.getInstance().textRenderer.getWidth((Text) string);
	}

	public static int getStringWidth(String string) {
		return MinecraftClient.getInstance().textRenderer.getWidth(string);
	}

	private static TextRenderer getTextRenderer() {
		return MinecraftClient.getInstance().textRenderer;
	}

}
