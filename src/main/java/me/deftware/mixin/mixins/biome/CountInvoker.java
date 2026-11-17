package me.deftware.mixin.mixins.biome;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RepeatingPlacement.class)
public interface CountInvoker {

    @Invoker("count")
    int getInvokedCount(RandomSource random, BlockPos pos);

}
