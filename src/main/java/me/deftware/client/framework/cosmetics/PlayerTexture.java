package me.deftware.client.framework.cosmetics;

import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.texture.PlayerSkinTextureDownloader;

import java.io.File;

public interface PlayerTexture {

    MinecraftIdentifier getCapeTexture();

    static void load(MinecraftIdentifier identifier, File cache) {
        PlayerSkinTextureDownloader.downloadAndRegisterTexture(identifier, cache.toPath(), null, false);
    }

}
