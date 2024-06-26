package me.deftware.mixin.mixins.biome;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlacedFeature.class)
public interface PlacedFeatureAccessor {

    @Accessor("feature")
    RegistryEntry<ConfiguredFeature<?, ?>> getFeature();

}
