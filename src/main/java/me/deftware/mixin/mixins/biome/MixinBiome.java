package me.deftware.mixin.mixins.biome;

import me.deftware.client.framework.world.gen.BiomeDecorator;
import me.deftware.client.framework.world.gen.DecoratorConfig;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Extracts all biome generation data
 */
@Mixin(BuiltinBiomes.class)
public class MixinBiome {

    @Inject(method = "register", at = @At("RETURN"))
    private static void onRegister(RegistryKey<Biome> key, Biome biome, CallbackInfoReturnable<Biome> cir) {
        register(biome, key.getValue());
    }

    @Unique
    private static void register(Biome biome, Identifier identifier) {
        BiomeDecorator decorator = new BiomeDecorator();
        // System.out.printf("== Biome %s ==\n", identifier.getPath());
        parseFeature(biome.getGenerationSettings(), GenerationStep.Feature.UNDERGROUND_ORES, decorator);
        parseFeature(biome.getGenerationSettings(), GenerationStep.Feature.UNDERGROUND_DECORATION, decorator);
        BiomeDecorator.BIOME_DECORATORS.put(identifier, decorator);
    }

    @Unique
    private static void parseFeature(GenerationSettings generationSettings, GenerationStep.Feature biomeFeature, BiomeDecorator decorator) {
        /* Empty  */
    }

}
