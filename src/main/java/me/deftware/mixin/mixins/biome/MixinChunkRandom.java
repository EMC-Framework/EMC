package me.deftware.mixin.mixins.biome;

import me.deftware.client.framework.world.chunk.ChunkGenerationRandom;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldgenRandom.class)
public abstract class MixinChunkRandom implements ChunkGenerationRandom {

    @Override
    public long _setPopulationSeed(long worldSeed, int blockX, int blockZ) {
        return ((WorldgenRandom) (Object) this).setDecorationSeed(worldSeed, blockX, blockZ);
    }

    @Override
    public void _setDecoratorSeed(long populationSeed, int index, int step) {
        ((WorldgenRandom) (Object) this).setFeatureSeed(populationSeed, index, step);
    }

    @Override
    public int _nextInt(int bound) {
        return ((WorldgenRandom) (Object) this).nextInt(bound);
    }

    @Override
    public float _nextFloat() {
        return ((WorldgenRandom) (Object) this).nextFloat();
    }

    @Override
    public double _nextDouble() {
        return ((WorldgenRandom) (Object) this).nextDouble();
    }

}
