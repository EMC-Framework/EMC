package me.deftware.client.framework.cosmetics;

import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ThreadDownloadImageData;
import net.minecraft.client.resources.DefaultPlayerSkin;

import java.io.File;

public interface PlayerTexture {

    MinecraftIdentifier getCapeTexture();

    static void load(MinecraftIdentifier identifier, File cache) {
        ThreadDownloadImageData texture = new ThreadDownloadImageData(cache, null,
                DefaultPlayerSkin.getDefaultSkinLegacy(), null);
        Minecraft.getInstance().getTextureManager().loadTexture(identifier, texture);
    }

}
