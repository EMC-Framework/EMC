package me.deftware.client.framework.world.chunk;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public interface ChunkGenerationRandom extends Randomizer {

    long _setPopulationSeed(long worldSeed, int blockX, int blockZ);

    void _setDecoratorSeed(long populationSeed, int index, int step);

    static ChunkGenerationRandom create(long seed) {
        RandomSource random = WorldgenRandom.Algorithm.XOROSHIRO.newInstance(seed);
        WorldgenRandom chunkRandom = new WorldgenRandom(random);
        return (ChunkGenerationRandom) chunkRandom;
    }

}
