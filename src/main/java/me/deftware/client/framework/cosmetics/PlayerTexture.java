package me.deftware.client.framework.cosmetics;

import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.class_10538;

import java.io.File;

public interface PlayerTexture {

    MinecraftIdentifier getCapeTexture();

    static void load(MinecraftIdentifier identifier, File cache) {
        class_10538.method_65861(identifier, cache.toPath(), null, false);
    }

}
