package me.deftware.client.framework.render.texture;

import com.mojang.blaze3d.systems.RenderSystem;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import org.lwjgl.opengl.GL11;

/**
 * @author Deftware
 */
public class TexUtil {

    public static void bindTexture(MinecraftIdentifier texture) {
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture);
    }

    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        DrawableHelper.blit(x, y, u, v, width, height, (int) textureWidth, (int) textureHeight);
    }

    public static int glGenTextures() {
        return GL11.glGenTextures();
    }

    public static void deleteTexture(int id) {
        RenderSystem.deleteTexture(id);
    }

}
