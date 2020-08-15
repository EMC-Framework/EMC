package me.deftware.client.framework.utils.render;

import me.deftware.client.framework.wrappers.IResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class TexUtil {
    
    public static void bindTexture(IResourceLocation texture) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    }

    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, (int) textureWidth, (int) textureHeight);
    }

    public static int glGenTextures() {
        return GlStateManager.generateTexture();
    }

    public static void deleteTexture(int id) {
        GlStateManager.deleteTexture(id);
    }

}
