package me.deftware.client.framework.world.chunk;

import net.minecraft.world.gen.ChunkRandom;

public interface ChunkGenerationRandom extends Randomizer {

    long _setPopulationSeed(long worldSeed, int blockX, int blockZ);

    void _setDecoratorSeed(long populationSeed, int index, int step);

    static ChunkGenerationRandom create(long seed) {
        return (ChunkGenerationRandom) new ChunkRandom(seed);
    }

}
