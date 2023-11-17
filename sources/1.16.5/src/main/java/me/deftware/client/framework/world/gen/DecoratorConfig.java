package me.deftware.client.framework.world.gen;

import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.chunk.Randomizer;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.UniformIntDistribution;

import java.util.*;
import java.util.function.Function;

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
    private Function<Random, Integer> heightProvider;

    /**
     * How many times the block generation should be repeated
     */
    private UniformIntDistribution repeat = UniformIntDistribution.of(1);

    /**
     * The size of a generation
     */
    private int size;

    /**
     * Specifies the chance a block will be discarded if it's spawned in the air
     */
    private float discardOnAirChance = 0;

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

    public int getY(Randomizer random, DecoratorContext context) {
        return this.heightProvider.apply((Random) random);
    }

    public int getFeature() {
        return feature.ordinal();
    }

    public int getRepeat(Randomizer random) {
        return this.repeat.getValue((Random) random);
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
