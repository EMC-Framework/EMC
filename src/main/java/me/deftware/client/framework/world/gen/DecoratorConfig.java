package me.deftware.client.framework.world.gen;

import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.world.block.Block;
import me.deftware.mixin.mixins.biome.CountInvoker;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.heightprovider.HeightProvider;

import java.util.*;

/**
 * Represents Biome ore generation
 *
 * @author Deftware
 */
@Setter
@Getter
public class DecoratorConfig {

    /**
     * The feature registry id
     */
    private String id = "unknown";

    /**
     * Feature config index
     */
    private int index;

    /**
     * The min and max Y level the block can spawn in
     */
    private HeightProvider heightProvider;

    /**
     * How many times the block generation should be repeated
     */
    private CountInvoker repeat = (CountInvoker) CountPlacementModifier.of(1);

    /**
     * The size of a generation
     */
    private int size;

    /**
     * Specifies the chance a block will be discarded if it's spawned in the air
     */
    private float discardOnAirChance;

    /**
     * List of all blocks this decorator creates
     */
    private final Set<Block> blockList = new HashSet<>();

    /**
     * The structure of this decorator
     */
    private StructureType structureType = StructureType.Uniform;

    /**
     * The generation method
     */
    private FeatureType featureType = FeatureType.Ore;

    /**
     * The generation type
     */
    private final GenerationStep.Feature feature;

    public DecoratorConfig(int index, GenerationStep.Feature feature) {
        this.index = index;
        this.feature = feature;
    }

    public int getY(Random random, DecoratorContext context) {
        return this.heightProvider.get(random, context.getHeightContext());
    }

    public int getFeature() {
        return feature.ordinal();
    }

    public int getRepeat(Random random) {
        return this.repeat.getInvokedCount(random, null);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "[ ", " ]")
                .add("id=" + this.id)
                .add("index=" + this.index)
                .add("size=" + this.size)
                .add("heightType=" + this.structureType.name())
                .add("feature=" + this.feature.name())
                .add("generator=" + this.featureType.name())
                .add("blocks=[" + String.join(",", this.blockList.stream().map(Block::getIdentifierKey).toArray(String[]::new)) + "]")
                .toString();
    }

    public enum StructureType {
        Uniform,
        Trapezoid
    }

    public enum FeatureType {
        Ore,
        ScatteredOre,
        Emerald
    }

}
