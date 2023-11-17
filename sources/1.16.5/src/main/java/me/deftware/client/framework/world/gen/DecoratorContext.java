package me.deftware.client.framework.world.gen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.ApiStatus;

public interface DecoratorContext {

    @ApiStatus.Internal
    Chunk getChunk();

    static DecoratorContext create() {
        Chunk chunk = MinecraftClient.getInstance().world.getChunk(MinecraftClient.getInstance().player.getBlockPos());
        return () -> chunk;
    }

}
