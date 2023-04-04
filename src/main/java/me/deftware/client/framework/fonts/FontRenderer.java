package me.deftware.client.framework.fonts;

import me.deftware.client.framework.message.Message;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author Deftware
 */
public class FontRenderer {

	private static final MatrixStack stack = new MatrixStack();

	public static void drawString(Message text, int x, int y, int color) {
		MinecraftClient.getInstance().textRenderer.draw(stack, (Text) text, x, y, color);
	}

	public static void drawCenteredString(Message text, int x, int y, int color) {
		MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, (Text) text, x - MinecraftClient.getInstance().textRenderer.getWidth((Text) text) / 2f, y, color);
	}

	public static void drawStringWithShadow(Message text, int x, int y, int color) {
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
