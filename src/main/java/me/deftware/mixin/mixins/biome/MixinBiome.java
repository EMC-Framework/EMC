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
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.DepthAverageDecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
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
                List<ConfiguredFeature<?, ?>> features = feature.getConfig().method_30649().collect(Collectors.toList());
                for (ConfiguredFeature<?, ?> f : features) {
                    parseConfig(f.config, data);
                    if (f.getFeature() instanceof NoSurfaceOreFeature) {
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
        if (featureConfig instanceof DecoratedFeatureConfig) {
            ConfiguredDecorator<?> decorator = ((DecoratedFeatureConfig) featureConfig).decorator;
            net.minecraft.world.gen.decorator.DecoratorConfig decoratorConfig = decorator.getConfig();
            if (decoratorConfig instanceof RangeDecoratorConfig) {
                RangeDecoratorConfig rangeDecoratorConfig = (RangeDecoratorConfig) decoratorConfig;
                data.setHeightProvider(random -> random.nextInt(rangeDecoratorConfig.maximum - rangeDecoratorConfig.topOffset) + rangeDecoratorConfig.bottomOffset);
            } else if (decoratorConfig instanceof DepthAverageDecoratorConfig) {
                DepthAverageDecoratorConfig depthAverageDecoratorConfig = (DepthAverageDecoratorConfig) decoratorConfig;
                int i = depthAverageDecoratorConfig.baseline;
                int j = depthAverageDecoratorConfig.spread;
                data.setHeightProvider(random -> random.nextInt(j) + random.nextInt(j) - j + i);
            } else if (decoratorConfig instanceof CountConfig) {
                data.setRepeat(((CountConfig) decoratorConfig).getCount());
            }
        } else if (featureConfig instanceof OreFeatureConfig) {
            OreFeatureConfig oreFeatureConfig = (OreFeatureConfig) featureConfig;
            data.setSize(oreFeatureConfig.size);
            // data.setDiscardOnAirChance(oreFeatureConfig.discardOnAirChance);
            Block block = (Block) oreFeatureConfig.state.getBlock();
            if (block != null) {
                data.getBlockList().add(block);
            }
        }
    }

}
