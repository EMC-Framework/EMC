package me.deftware.client.framework.world.block;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.world.EnumFacing;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.enums.ChestType;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.*;

public class BlockUtils {

    public static DoubleBlockType getDoubleBlockType(BlockPosition position) {
        net.minecraft.block.BlockState state = MinecraftClient.getInstance().world.getBlockState((BlockPos) position);
        if (state.getBlock() instanceof ChestBlock) {
            ChestType type = state.get(ChestBlock.CHEST_TYPE);
            return DoubleBlockType.values()[type.ordinal()];
        }
        return DoubleBlockType.Single;
    }

    public static EnumFacing getBlockFacing(BlockPosition position) {
        net.minecraft.block.BlockState state = MinecraftClient.getInstance().world.getBlockState((BlockPos) position);
        if (state.getBlock() instanceof ChestBlock) {
            return EnumFacing.fromMinecraft(ChestBlock.getFacing(state));
        }
        return null;
    }

    public static boolean isNormalCube(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof BambooBlock ||
                block instanceof PistonExtensionBlock ||
                block instanceof ScaffoldingBlock ||
                block instanceof ShulkerBoxBlock) {
            return false;
        }
        return net.minecraft.block.Block.isShapeFullCube(((net.minecraft.block.BlockState) state)
                .getCollisionShape(null, null));
    }

    public enum DoubleBlockType {
        Single, First, Second
    }

    public static class FluidState {

        public static boolean hasFluid(BlockState blockState) {
            return !((net.minecraft.block.BlockState) blockState).getFluidState().isEmpty();
        }

        public static boolean isFluidWater(BlockState blockState) {
            Fluid state = ((net.minecraft.block.BlockState) blockState).getFluidState().getFluid();
            return state == Fluids.WATER || state == Fluids.FLOWING_WATER;
        }

        public static boolean isFluidFlowing(BlockState blockState) {
            Fluid state = ((net.minecraft.block.BlockState) blockState).getFluidState().getFluid();
            return state == Fluids.FLOWING_LAVA || state == Fluids.FLOWING_WATER;
        }


    }

    public static class State {

        public static int getSnowLayers(BlockState blockState) {
            if (blockState.getBlock() instanceof SnowBlock) {
                return ((net.minecraft.block.BlockState) blockState).get(SnowBlock.LAYERS);
            }
            return 0;
        }

        public static boolean isBottomSlab(BlockState blockState) {
            if (blockState.getBlock() instanceof SlabBlock) {
                return ((net.minecraft.block.BlockState) blockState).get(SlabBlock.TYPE) == SlabType.BOTTOM;
            }
            return false;
        }

    }

}
