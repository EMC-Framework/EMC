package me.deftware.client.framework.fonts;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author Deftware
 */
public class FontRenderer {

	private final static MatrixStack stack = new MatrixStack();
	
	public static void drawString(GLX context, Message text, int x, int y, int color) {
		MinecraftClient.getInstance().textRenderer.draw(stack, (Text) text, x, y, color);
	}

	public static void drawCenteredString(GLX context, Message text, int x, int y, int color) {
		MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, (Text) text, x - MinecraftClient.getInstance().textRenderer.getWidth((Text) text) / 2f, y, color);
	}

	public static void drawStringWithShadow(GLX context, Message text, int x, int y, int color) {
		MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, (Text) text, x, y, color);
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

}
