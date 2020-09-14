package me.deftware.client.framework.render.texture;

import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * @author Deftware
 */
public class TexUtil {

    public static void bindTexture(MinecraftIdentifier texture) {
        net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    }

    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
       Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, (int) textureWidth, (int) textureHeight);
    }

    public static int glGenTextures() {
        return GL11.glGenTextures();
    }

    public static void deleteTexture(int id) {
        GlStateManager.deleteTexture(id);
    }

}
