package me.deftware.client.framework.world.chunk;

import net.minecraft.util.math.random.Random;

public interface Randomizer extends Random {

    int _nextInt(int bound);

    float _nextFloat();

    double _nextDouble();

}
