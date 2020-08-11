package me.deftware.client.framework.wrappers.render;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.client.Minecraft;

/**
 * Wrapper for Minecrafts built in font renderer
 *
 * @author Deftware
 */
public class IFontRenderer {

    public static void drawString(ChatMessage text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawString(text.toString(true), x, y, color);
    }

    public static void drawCenteredString(ChatMessage text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text.toString(true), x - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text.toString(true)) / 2f, y, color);
    }

    public static void drawStringWithShadow(ChatMessage text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text.toString(true), x, y, color);
    }

    public static int getFontHeight() {
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    }

    public static int getStringWidth(ChatMessage string) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(string.toString(true));
    }

    @Deprecated
    public static void drawString(String text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color);
    }

    @Deprecated
    public static void drawCenteredString(String text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2f, y, color);
    }

    @Deprecated
    public static void drawStringWithShadow(String text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
    }

    @Deprecated
    public static int getStringWidth(String string) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(string);
    }

}
