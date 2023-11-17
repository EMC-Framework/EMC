package me.deftware.client.framework.cosmetics;

import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.util.DefaultSkinHelper;

import java.io.File;

public interface PlayerTexture {

    MinecraftIdentifier getCapeTexture();

    static void load(MinecraftIdentifier identifier, File cache) {
        PlayerSkinTexture texture = new PlayerSkinTexture(cache, null,
                DefaultSkinHelper.getTexture(), false, null);
        MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, texture);
    }

}
