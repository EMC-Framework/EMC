package me.deftware.client.framework.fonts;

import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.Minecraft;
import me.deftware.client.framework.message.Message;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Deftware
 */
public class FontRenderer {

	public static void drawString(GLX context, Message text, int x, int y, int color) {
		Minecraft.getInstance().fontRenderer.drawString(((ITextComponent) text).getFormattedText(), x, y, color);
	}

	public static void drawCenteredString(GLX context, Message text, int x, int y, int color) {
		net.minecraft.client.Minecraft.getInstance().fontRenderer.drawStringWithShadow(((ITextComponent) text).getFormattedText(), x - net.minecraft.client.Minecraft.getInstance().fontRenderer.getStringWidth(text.string()) / 2f, y, color);
	}

	public static void drawStringWithShadow(GLX context, Message text, int x, int y, int color) {
		net.minecraft.client.Minecraft.getInstance().fontRenderer.drawStringWithShadow(((ITextComponent) text).getFormattedText(), x, y, color);
	}

	public static int getFontHeight() {
		return net.minecraft.client.Minecraft.getInstance().fontRenderer.FONT_HEIGHT;
	}

	public static int getStringWidth(Message string) {
		return net.minecraft.client.Minecraft.getInstance().fontRenderer.getStringWidth(string.string());
	}

	public static int getStringWidth(String string) {
		return net.minecraft.client.Minecraft.getInstance().fontRenderer.getStringWidth(string);
	}

}
