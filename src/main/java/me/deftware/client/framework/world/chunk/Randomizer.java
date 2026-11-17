package me.deftware.client.framework.world.chunk;

import net.minecraft.util.RandomSource;

public interface Randomizer extends RandomSource {

    int _nextInt(int bound);

    float _nextFloat();

    double _nextDouble();

}
