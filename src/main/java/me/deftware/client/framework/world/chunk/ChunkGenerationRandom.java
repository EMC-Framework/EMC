package me.deftware.client.framework.world.chunk;

import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.gen.random.Xoroshiro128PlusPlusRandom;

public interface ChunkGenerationRandom extends Randomizer {

    long _setPopulationSeed(long worldSeed, int blockX, int blockZ);

    void _setDecoratorSeed(long populationSeed, int index, int step);

    static ChunkGenerationRandom create(long seed) {
        Xoroshiro128PlusPlusRandom random = new Xoroshiro128PlusPlusRandom(seed);
        ChunkRandom chunkRandom = new ChunkRandom(random);
        return (ChunkGenerationRandom) chunkRandom;
    }

}
