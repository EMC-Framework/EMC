package me.deftware.mixin.shared;

import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;

/**
 * @author Deftware
 */
public class BlockManagement {

    public static final ThreadLocal<Boolean> isRenderingOpacityBlock = ThreadLocal.withInitial(() -> false);

    public static Optional<Boolean> shouldDrawSide(BlockState state, BlockGetter world, BlockPos pos, Direction facing) {
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = BuiltInRegistries.BLOCK.getId(state.getBlock());
            if (blockProperties.contains(id) && blockProperties.get(id).isRender()) {
                if (!blockProperties.isExposedOnly() || isAnySideTouchingBlock(pos, world, Blocks.AIR, Blocks.CAVE_AIR))
                    return Optional.of(true);
            } else {
                if (
                        (blockProperties.isDisableCaveRendering() && isAnySideTouchingBlock(pos, world, Blocks.CAVE_AIR, Blocks.WATER, Blocks.LAVA)) || !blockProperties.isOpacityMode()
                )
                    return Optional.of(false);
            }
        }
        return Optional.empty();
    }

    public static boolean isAnySideTouchingBlock(BlockPos pos, BlockGetter world, Block... blocks) {
        for (Direction direction : Direction.values()) {
            try {
                BlockState blockState = world.getBlockState(pos.relative(direction, 1));
                for (Block block : blocks) {
                    if (blockState.getBlock() == block) {
                        return true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
                // Happens if an offset block position is outside a valid chunk
                // TODO: Possibly verify that an index is valid using
                //  ChunkRendererRegion.getIndex < ChunkRendererRegion.chunks.length
            }
        }
        return false;
    }

}
