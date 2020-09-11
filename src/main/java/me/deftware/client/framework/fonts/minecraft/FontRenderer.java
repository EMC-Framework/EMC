package me.deftware.client.framework.fonts.minecraft;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.client.Minecraft;

/**
 * @author Deftware
 */
public class FontRenderer {

	public static void drawString(ChatMessage text, int x, int y, int color) {
		Minecraft.getInstance().fontRenderer.drawString(text.toString(true), x, y, color);
	}

	public static void drawCenteredString(ChatMessage text, int x, int y, int color) {
		net.minecraft.client.Minecraft.getInstance().fontRenderer.drawStringWithShadow(text.toString(true), x - net.minecraft.client.Minecraft.getInstance().fontRenderer.getStringWidth(text.toString(true)) / 2f, y, color);
	}

	public static void drawStringWithShadow(ChatMessage text, int x, int y, int color) {
		net.minecraft.client.Minecraft.getInstance().fontRenderer.drawStringWithShadow(text.toString(true), x, y, color);
	}

	public static int getFontHeight() {
		return net.minecraft.client.Minecraft.getInstance().fontRenderer.FONT_HEIGHT;
	}

	public static int getStringWidth(ChatMessage string) {
		return net.minecraft.client.Minecraft.getInstance().fontRenderer.getStringWidth(string.toString(true));
	}

	public static int getStringWidth(String string) {
		return net.minecraft.client.Minecraft.getInstance().fontRenderer.getStringWidth(string);
	}

}
