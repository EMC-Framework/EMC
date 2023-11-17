package me.deftware.mixin.mixins.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.placementmodifier.AbstractCountPlacementModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCountPlacementModifier.class)
public interface CountInvoker {

    @Invoker("getCount")
    int getInvokedCount(Random random, BlockPos pos);

}
