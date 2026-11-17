package me.deftware.client.framework.world.block;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.world.EnumFacing;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class BlockUtils {

    public static DoubleBlockType getDoubleBlockType(BlockPosition position) {
        var state = Minecraft.getInstance().level.getBlockState((BlockPos) position);
        if (state.getBlock() instanceof ChestBlock) {
            var type = ChestBlock.getBlockType(state);
            return DoubleBlockType.values()[type.ordinal()];
        }
        return DoubleBlockType.Single;
    }

    public static EnumFacing getBlockFacing(BlockPosition position) {
        var state = Minecraft.getInstance().level.getBlockState((BlockPos) position);
        if (state.getBlock() instanceof ChestBlock) {
            return EnumFacing.fromMinecraft(ChestBlock.getConnectedDirection(state));
        }
        return null;
    }

    public static boolean isNormalCube(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof BambooStalkBlock ||
                block instanceof MovingPistonBlock ||
                block instanceof ScaffoldingBlock ||
                block instanceof ShulkerBoxBlock ||
                block instanceof PointedDripstoneBlock ||
                block instanceof AmethystClusterBlock) {
            return false;
        }
        return net.minecraft.world.level.block.Block.isShapeFullBlock(((net.minecraft.world.level.block.state.BlockState) state)
                .getCollisionShape(null, null));
    }

    public enum DoubleBlockType {
        Single, First, Second
    }

    public static class FluidState {

        public static boolean hasFluid(BlockState blockState) {
            return !((net.minecraft.world.level.block.state.BlockState) blockState).getFluidState().isEmpty();
        }

        public static boolean isFluidWater(BlockState blockState) {
            Fluid state = ((net.minecraft.world.level.block.state.BlockState) blockState).getFluidState().getType();
            return state == Fluids.WATER || state == Fluids.FLOWING_WATER;
        }

        public static boolean isFluidFlowing(BlockState blockState) {
            Fluid state = ((net.minecraft.world.level.block.state.BlockState) blockState).getFluidState().getType();
            return state == Fluids.FLOWING_LAVA || state == Fluids.FLOWING_WATER;
        }


    }

    public static class State {

        public static int getSnowLayers(BlockState blockState) {
            if (blockState.getBlock() instanceof SnowLayerBlock) {
                return ((net.minecraft.world.level.block.state.BlockState) blockState).getValue(SnowLayerBlock.LAYERS);
            }
            return 0;
        }

        public static boolean isBottomSlab(BlockState blockState) {
            if (blockState.getBlock() instanceof SlabBlock) {
                return ((net.minecraft.world.level.block.state.BlockState) blockState).getValue(SlabBlock.TYPE) == SlabType.BOTTOM;
            }
            return false;
        }

    }

}
