package me.deftware.mixin.shared;

import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Deftware
 */
public class BlockManagement {

    public static void shouldDrawSide(BlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> callback) {
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive()) {
            int id = Block.blockRegistry.getIDForObject(state.getBlock());
            if (blockProperties.contains(id) && blockProperties.get(id).isRender()) {
                if (!blockProperties.isExposedOnly() || isAnySideTouchingBlock(pos, worldIn, Blocks.air))
                    callback.setReturnValue(true);
            } else {
                // FIXME: blockProperties.isDisableCaveRendering() && isAnySideTouchingBlock(pos, world, Blocks.CAVE_AIR, Blocks.WATER, Blocks.LAVA)) ||
                if (!blockProperties.isOpacityMode())
                    callback.setReturnValue(false);
            }
        }
    }

    public static boolean isAnySideTouchingBlock(BlockPos pos, IBlockAccess world, Block... blocks) {
        for (EnumFacing direction : EnumFacing.values()) {
            IBlockState blockState = world.getBlockState(
                    pos.offset(direction)
            );
            for (Block block : blocks)
                if (blockState.getBlock() == block)
                    return true;
        }
        return false;
    }

}
