package me.deftware.mixin.mixins.biome;

import net.minecraft.world.level.levelgen.placement.RarityFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RarityFilter.class)
public interface RarityAccessor {

    @Accessor("chance")
    int getChance();

}
