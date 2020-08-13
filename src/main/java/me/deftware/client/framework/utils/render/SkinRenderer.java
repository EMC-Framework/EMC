package me.deftware.client.framework.utils.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class SkinRenderer {

    public static void drawAltBody(String name, String uuid, int x, int y, int width, int height) {
        try {
            RenderUtils.bindSkinTexture(name, uuid);
            boolean slim = DefaultSkinHelper.getModel(UUID.fromString(uuid)).equals("slim");

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 1);

            // Face
            x = x + width / 4;
            int w = width / 2;
            int h = height / 4;
            int fw = height * 2;
            int fh = height * 2;
            float u = height / 4f;
            float v = height / 4f;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Hat
            w = width / 2;
            h = height / 4;
            u = height / 4f * 5;
            v = height / 4f;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Chest
            y = y + height / 4;
            w = width / 2;
            h = height / 8 * 3;
            u = height / 4f * 2.5F;
            v = height / 4f * 2.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Jacket
            w = width / 2;
            h = height / 8 * 3;
            u = height / 4f * 2.5F;
            v = height / 4f * 4.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Left Arm
            x = x - width / 16 * (slim ? 3 : 4);
            y = y + (slim ? height / 32 : 0);
            w = width / 16 * (slim ? 3 : 4);
            h = height / 8 * 3;
            u = height / 4f * 5.5F;
            v = height / 4f * 2.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Left Sleeve
            w = width / 16 * (slim ? 3 : 4);
            h = height / 8 * 3;
            u = height / 4f * 5.5F;
            v = height / 4f * 4.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Right Arm
            x = x + width / 16 * (slim ? 11 : 12);
            w = width / 16 * (slim ? 3 : 4);
            h = height / 8 * 3;
            u = height / 4f * 5.5F;
            v = height / 4f * 2.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Right Sleeve
            w = width / 16 * (slim ? 3 : 4);
            h = height / 8 * 3;
            u = height / 4f * 5.5F;
            v = height / 4f * 4.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Left Leg
            x = x - width / 2;
            y = y + height / 32 * (slim ? 11 : 12);
            w = width / 4;
            h = height / 8 * 3;
            u = height / 4f * 0.5F;
            v = height / 4f * 2.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Left Pants
            w = width / 4;
            h = height / 8 * 3;
            u = height / 4f * 0.5F;
            v = height / 4f * 4.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Right Leg
            x = x + width / 4;
            w = width / 4;
            h = height / 8 * 3;
            u = height / 4f * 0.5F;
            v = height / 4f * 2.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            // Right Pants
            w = width / 4;
            h = height / 8 * 3;
            u = height / 4f * 0.5F;
            v = height / 4f * 4.5F;
            Screen.blit(x, y, u, v, w, h, fw, fh);

            GL11.glDisable(GL11.GL_BLEND);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
