package me.deftware.client.framework.render.texture;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Deftware
 */
public class SkinRenderer {

    private static final HashMap<String, Pair<Boolean, ResourceLocation>> loadedSkins = new HashMap<>();

    public static void bindSkinTexture(String name, String uuid) {
        GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
        if(loadedSkins.containsKey(name)) {
            if (loadedSkins.get(name).getFirst()) {
                Minecraft.getInstance().getTextureManager().bindTexture(loadedSkins.get(name).getSecond());
            } else {
                Minecraft.getInstance().getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkin(profile.getId()));
            }
        } else {
            loadedSkins.put(name, new Pair<>(false, null));
            try {
                Minecraft.getInstance().getSkinManager().loadProfileTextures(profile, (type, identifier, minecraftProfileTexture) -> {
                    if (type == MinecraftProfileTexture.Type.SKIN) {
                        loadedSkins.put(name, new Pair<>(true, identifier));
                    }
                }, true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void drawAltFace(String name, String uuid, int x, int y, int w, int h) {
        try {
            bindSkinTexture(name, uuid);
            glEnable(GL_BLEND);
            glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 24, 24, w, h, 192, 192);
            Gui.drawModalRectWithCustomSizedTexture(x, y, 120, 24, w, h, 192, 192);
            glDisable(GL_BLEND);
        } catch (Exception ignored) { }
    }

    public static void drawAltBody(String name, String uuid, int x, int y, int width, int height) {
        try {
            bindSkinTexture(name, uuid);
            boolean slim = DefaultPlayerSkin.getSkinType(UUID.fromString(uuid)).equals("slim");

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
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Hat
            w = width / 2;
            h = height / 4;
            u = height / 4f * 5;
            v = height / 4f;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Chest
            y = y + height / 4;
            w = width / 2;
            h = height / 8 * 3;
            u = height / 4f * 2.5F;
            v = height / 4f * 2.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Jacket
            w = width / 2;
            h = height / 8 * 3;
            u = height / 4f * 2.5F;
            v = height / 4f * 4.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Left Arm
            x = x - width / 16 * (slim ? 3 : 4);
            y = y + (slim ? height / 32 : 0);
            w = width / 16 * (slim ? 3 : 4);
            h = height / 8 * 3;
            u = height / 4f * 5.5F;
            v = height / 4f * 2.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Left Sleeve
            w = width / 16 * (slim ? 3 : 4);
            h = height / 8 * 3;
            u = height / 4f * 5.5F;
            v = height / 4f * 4.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Right Arm
            x = x + width / 16 * (slim ? 11 : 12);
            w = width / 16 * (slim ? 3 : 4);
            h = height / 8 * 3;
            u = height / 4f * 5.5F;
            v = height / 4f * 2.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Right Sleeve
            w = width / 16 * (slim ? 3 : 4);
            h = height / 8 * 3;
            u = height / 4f * 5.5F;
            v = height / 4f * 4.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Left Leg
            x = x - width / 2;
            y = y + height / 32 * (slim ? 11 : 12);
            w = width / 4;
            h = height / 8 * 3;
            u = height / 4f * 0.5F;
            v = height / 4f * 2.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Left Pants
            w = width / 4;
            h = height / 8 * 3;
            u = height / 4f * 0.5F;
            v = height / 4f * 4.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Right Leg
            x = x + width / 4;
            w = width / 4;
            h = height / 8 * 3;
            u = height / 4f * 0.5F;
            v = height / 4f * 2.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            // Right Pants
            w = width / 4;
            h = height / 8 * 3;
            u = height / 4f * 0.5F;
            v = height / 4f * 4.5F;
            Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, fw, fh);

            GL11.glDisable(GL11.GL_BLEND);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
