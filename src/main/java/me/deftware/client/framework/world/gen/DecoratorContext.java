package me.deftware.client.framework.world.gen;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.jetbrains.annotations.ApiStatus;

public interface DecoratorContext {

    @ApiStatus.Internal
    WorldGenerationContext getHeightContext();

    static DecoratorContext create() {
        ChunkAccess chunk = Minecraft.getInstance().level.getChunk(Minecraft.getInstance().player.blockPosition());
        WorldGenerationContext heightContext = new WorldGenerationContext(null, chunk);
        return () -> heightContext;
    }

}
