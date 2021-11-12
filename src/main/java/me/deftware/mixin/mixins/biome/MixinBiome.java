package me.deftware.mixin.mixins.biome;

import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.gen.BiomeDecorator;
import me.deftware.client.framework.world.gen.DecoratorConfig;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Extracts all biome generation data
 */
@Mixin(BuiltinBiomes.class)
public class MixinBiome {

    @Inject(method = "register", at = @At("RETURN"))
    private static void onRegister(int id, RegistryKey<Biome> registryKey, Biome biome, CallbackInfoReturnable<Biome> cir) {
        register(biome, registryKey.getValue());
    }

    @Unique
    private static void register(Biome biome, Identifier identifier) {
        BiomeDecorator decorator = new BiomeDecorator();
        parseFeature(biome.getGenerationSettings(), GenerationStep.Feature.UNDERGROUND_ORES, decorator);
        parseFeature(biome.getGenerationSettings(), GenerationStep.Feature.UNDERGROUND_DECORATION, decorator);
        BiomeDecorator.BIOME_DECORATORS.put(identifier, decorator);
    }

    @Unique
    private static void parseFeature(GenerationSettings generationSettings, GenerationStep.Feature biomeFeature, BiomeDecorator decorator) {
        if (biomeFeature.ordinal() >= generationSettings.getFeatures().size())
            return;
        List<Supplier<ConfiguredFeature<?, ?>>> list = generationSettings.getFeatures().get(biomeFeature.ordinal());
        for (int i = 0; i < list.size(); i++) {
            ConfiguredFeature<?, ?> feature = list.get(i).get();
            Identifier identifier = BuiltinRegistries.CONFIGURED_FEATURE.getId(feature);
            if (identifier != null) {
                DecoratorConfig data = new DecoratorConfig(i, biomeFeature);
                List<ConfiguredFeature<?, ?>> features = feature.getConfig().getDecoratedFeatures().collect(Collectors.toList());
                for (ConfiguredFeature<?, ?> f : features) {
                    parseConfig(f.config, data);
                    if (f.getFeature() instanceof ScatteredOreFeature) {
                        data.setFeatureType(DecoratorConfig.FeatureType.ScatteredOre);
                    } else if (f.getFeature() instanceof EmeraldOreFeature) {
                        data.setFeatureType(DecoratorConfig.FeatureType.Emerald);
                    }
                }
                parseConfig(feature.config, data);
                BuiltinRegistries.CONFIGURED_FEATURE.getKey(feature).ifPresent(k -> data.setId(k.getValue().getPath()));
                decorator.getDecorators().add(data);
            }
        }
    }

    @Unique
    private static void parseConfig(FeatureConfig featureConfig, DecoratorConfig data) {
        if (featureConfig instanceof DecoratedFeatureConfig decoratedFeatureConfig) {
            ConfiguredDecorator<?> decorator = decoratedFeatureConfig.decorator;
            net.minecraft.world.gen.decorator.DecoratorConfig decoratorConfig = decorator.getConfig();
            if (decoratorConfig instanceof RangeDecoratorConfig rangeDecoratorConfig) {
                HeightProvider heightProvider = rangeDecoratorConfig.heightProvider;
                data.setHeightProvider(heightProvider);
            } else if (decoratorConfig instanceof CountConfig countConfig) {
                data.setRepeat(countConfig.getCount());
            }
        } else if (featureConfig instanceof OreFeatureConfig oreFeatureConfig) {
            data.setSize(oreFeatureConfig.size);
            data.setDiscardOnAirChance(oreFeatureConfig.discardOnAirChance);
            for (OreFeatureConfig.Target target : oreFeatureConfig.targets) {
                Block block = BlockRegistry.INSTANCE.getBlock(target.state.getBlock());
                if (block != null) {
                    data.getBlockList().add(block);
                }
            }
        }
    }

}
