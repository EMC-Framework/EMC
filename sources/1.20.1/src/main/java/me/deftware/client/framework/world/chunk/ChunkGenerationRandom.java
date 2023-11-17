package me.deftware.client.framework.world.chunk;

import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;

public interface ChunkGenerationRandom extends Randomizer {

    long _setPopulationSeed(long worldSeed, int blockX, int blockZ);

    void _setDecoratorSeed(long populationSeed, int index, int step);

    static ChunkGenerationRandom create(long seed) {
        Random random = ChunkRandom.RandomProvider.XOROSHIRO.create(seed);
        ChunkRandom chunkRandom = new ChunkRandom(random);
        return (ChunkGenerationRandom) chunkRandom;
    }

}
