package me.deftware.client.framework.fonts;

import net.minecraft.client.Minecraft;
import me.deftware.client.framework.message.Message;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Deftware
 */
public class FontRenderer {

	public static void drawString(Message text, int x, int y, int color) {
		Minecraft.getMinecraft().fontRenderer.drawString(((ITextComponent) text).getFormattedText(), x, y, color);
	}

	public static void drawCenteredString(Message text, int x, int y, int color) {
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(((ITextComponent) text).getFormattedText(), x - Minecraft.getMinecraft().fontRenderer.getStringWidth(text.string()) / 2f, y, color);
	}

	public static void drawStringWithShadow(Message text, int x, int y, int color) {
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(((ITextComponent) text).getFormattedText(), x, y, color);
	}

	public static int getFontHeight() {
		return 9;
	}

	public static int getStringWidth(Message string) {
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(string.string());
	}

	public static int getStringWidth(String string) {
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(string);
	}

}
