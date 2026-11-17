package me.deftware.client.framework.cosmetics;

import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import java.io.File;

public interface PlayerTexture {

    MinecraftIdentifier getCapeTexture();

    static void load(MinecraftIdentifier identifier, File cache) {
        var mc = net.minecraft.client.Minecraft.getInstance();
        new SkinTextureDownloader(mc.getProxy(), mc.getTextureManager(), mc)
                .downloadAndRegisterSkin(identifier, cache.toPath(), null, false);
    }

}
