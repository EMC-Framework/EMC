package me.deftware.mixin.mixins.biome;

import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.gen.BiomeDecorator;
import me.deftware.client.framework.world.gen.DecoratorConfig;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.CountDepthDecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Extracts all biome generation data
 */
@Mixin(Biomes.class)
public class MixinBiome {

    @Inject(method = "register", at = @At("RETURN"))
    private static void onRegister(int id, String key, Biome biome, CallbackInfoReturnable<Biome> cir) {
        register(biome, new Identifier(key));
    }

    @Unique
    private static void register(Biome biome, Identifier identifier) {
        BiomeDecorator decorator = new BiomeDecorator();
        parseFeature(biome, GenerationStep.Feature.UNDERGROUND_ORES, decorator);
        parseFeature(biome, GenerationStep.Feature.UNDERGROUND_DECORATION, decorator);
        BiomeDecorator.BIOME_DECORATORS.put(identifier, decorator);
    }

    @Unique
    private static void parseFeature(Biome biome, GenerationStep.Feature biomeFeature, BiomeDecorator decorator) {
        List<ConfiguredFeature<?, ?>> list = biome.getFeaturesForStep(biomeFeature);
        if (list == null)
            return;
        for (int i = 0; i < list.size(); i++) {
            ConfiguredFeature<?, ?> feature = list.get(i);
            DecoratorConfig data = new DecoratorConfig(i, biomeFeature);
            parseConfig(feature.config, data);
            decorator.getDecorators().add(data);
        }
    }

    @Unique
    private static void parseConfig(FeatureConfig featureConfig, DecoratorConfig data) {
        if (featureConfig instanceof DecoratedFeatureConfig) {
            ConfiguredDecorator<?> decorator = ((DecoratedFeatureConfig) featureConfig).decorator;
            net.minecraft.world.gen.decorator.DecoratorConfig decoratorConfig = decorator.config;
            if (decoratorConfig instanceof RangeDecoratorConfig) {
                RangeDecoratorConfig rangeDecoratorConfig = (RangeDecoratorConfig) decoratorConfig;
                data.setHeightProvider(random -> random.nextInt(rangeDecoratorConfig.maximum - rangeDecoratorConfig.topOffset) + rangeDecoratorConfig.bottomOffset);
                data.setRepeat(rangeDecoratorConfig.count);
            } else if (decoratorConfig instanceof CountDepthDecoratorConfig) {
                CountDepthDecoratorConfig depthAverageDecoratorConfig = (CountDepthDecoratorConfig) decoratorConfig;
                int i = depthAverageDecoratorConfig.baseline;
                int j = depthAverageDecoratorConfig.spread;
                data.setHeightProvider(random -> random.nextInt(j) + random.nextInt(j) - j + i);
                data.setRepeat(depthAverageDecoratorConfig.count);
            }
            parseConfig(((DecoratedFeatureConfig) featureConfig).feature.config, data);
        } else if (featureConfig instanceof OreFeatureConfig) {
            OreFeatureConfig oreFeatureConfig = (OreFeatureConfig) featureConfig;
            data.setSize(oreFeatureConfig.size);
            // data.setDiscardOnAirChance(oreFeatureConfig.discardOnAirChance);
            Block block = BlockRegistry.INSTANCE.getBlock(oreFeatureConfig.state.getBlock());
            if (block != null) {
                data.getBlockList().add(block);
            }
        }
    }

}
