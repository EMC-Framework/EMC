package me.deftware.client.framework.world.chunk;

import net.minecraft.util.math.random.AbstractRandom;

public interface Randomizer extends AbstractRandom {

    int _nextInt(int bound);

    float _nextFloat();

    double _nextDouble();

}
