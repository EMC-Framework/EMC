package me.deftware.mixin.mixins.biome;

import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Allows passing a null ChunkGenerator, and only
 * using the HeightViewLimit (Which Chunk implements)
 */
@Mixin(WorldGenerationContext.class)
public class MixinHeightContext {

    @Shadow
    @Final
    @Mutable
    private int minY;

    @Shadow
    @Final
    @Mutable
    private int height;

    @Redirect(method = "<init>(Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/world/level/LevelHeightAccessor;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;getMinY()I"))
    private int onGetChunkY(ChunkGenerator instance) {
        if (instance == null) {
            return -9999;
        }
        return instance.getMinY();
    }

    @Redirect(method = "<init>(Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/world/level/LevelHeightAccessor;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;getGenDepth()I"))
    private int onGetChunkHeight(ChunkGenerator instance) {
        if (instance == null) {
            return 9999;
        }
        return instance.getGenDepth();
    }

}
