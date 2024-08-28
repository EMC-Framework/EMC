package me.deftware.mixin.shared;

import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * @author Deftware
 */
public class BlockManagement {

    public static Optional<Boolean> shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction facing) {
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = Registries.BLOCK.getRawId(state.getBlock());
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

    public static boolean isAnySideTouchingBlock(BlockPos pos, BlockView world, Block... blocks) {
        for (Direction direction : Direction.values()) {
            BlockState blockState = world.getBlockState(
                    pos.offset(direction, 1)
            );
            for (Block block : blocks)
                if (blockState.getBlock() == block)
                    return true;
        }
        return false;
    }

}
