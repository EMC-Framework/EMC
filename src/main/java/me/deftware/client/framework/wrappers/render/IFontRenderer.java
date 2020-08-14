package me.deftware.client.framework.wrappers.render;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.client.MinecraftClient;

/**
 * Wrapper for Minecrafts built in font renderer
 *
 * @author Deftware
 */
public class IFontRenderer {

    public static void drawString(ChatMessage text, int x, int y, int color) {
        MinecraftClient.getInstance().textRenderer.draw(text.toString(true), x, y, color);
    }

    public static void drawCenteredString(ChatMessage text, int x, int y, int color) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(text.toString(true), x - MinecraftClient.getInstance().textRenderer.getStringWidth(text.toString(true)) / 2f, y, color);
    }

    public static void drawStringWithShadow(ChatMessage text, int x, int y, int color) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(text.toString(true), x, y, color);
    }

    public static int getFontHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    public static int getStringWidth(ChatMessage string) {
        return MinecraftClient.getInstance().textRenderer.getStringWidth(string.toString(true));
    }

    @Deprecated
    public static void drawString(String text, int x, int y, int color) {
        MinecraftClient.getInstance().textRenderer.draw(text, x, y, color);
    }

    @Deprecated
    public static void drawCenteredString(String text, int x, int y, int color) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(text, x - MinecraftClient.getInstance().textRenderer.getStringWidth(text) / 2f, y, color);
    }

    @Deprecated
    public static void drawStringWithShadow(String text, int x, int y, int color) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(text, x, y, color);
    }

    @Deprecated
    public static int getStringWidth(String string) {
        return MinecraftClient.getInstance().textRenderer.getStringWidth(string);
    }

}
