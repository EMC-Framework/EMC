package me.deftware.client.framework.world.block;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.world.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.init.Fluids;
import net.minecraft.state.properties.ChestType;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.*;

public class BlockUtils {

    public static DoubleBlockType getDoubleBlockType(BlockPosition position) {
        IBlockState state = Minecraft.getInstance().world.getBlockState((BlockPos) position);
        if (state.getBlock() instanceof BlockChest) {
            ChestType type = state.get(BlockChest.TYPE);
            return DoubleBlockType.values()[type.ordinal()];
        }
        return DoubleBlockType.Single;
    }

    public static EnumFacing getBlockFacing(BlockPosition position) {
        IBlockState state = Minecraft.getInstance().world.getBlockState((BlockPos) position);
        if (state.getBlock() instanceof BlockChest) {
            return EnumFacing.fromMinecraft(BlockChest.getDirectionToAttached(state));
        }
        return null;
    }

    public static boolean isNormalCube(BlockState state) {
        return false;
    }

    public enum DoubleBlockType {
        Single, First, Second
    }

    public static class FluidState {

        public static boolean hasFluid(BlockState blockState) {
            return !((net.minecraft.block.state.BlockState) blockState).getFluidState().isEmpty();
        }

        public static boolean isFluidWater(BlockState blockState) {
            Fluid state = ((net.minecraft.block.state.BlockState) blockState).getFluidState().getFluid();
            return state == Fluids.WATER || state == Fluids.FLOWING_WATER;
        }

        public static boolean isFluidFlowing(BlockState blockState) {
            Fluid state = ((net.minecraft.block.state.BlockState) blockState).getFluidState().getFluid();
            return state == Fluids.FLOWING_LAVA || state == Fluids.FLOWING_WATER;
        }


    }

    public static class State {

        public static int getSnowLayers(BlockState blockState) {
            if (blockState.getBlock() instanceof BlockSnow) {
                return ((net.minecraft.block.state.BlockState) blockState).get(BlockSnowLayer.LAYERS);
            }
            return 0;
        }

        public static boolean isBottomSlab(BlockState blockState) {
            if (blockState.getBlock() instanceof BlockSlab) {
                return ((net.minecraft.block.state.BlockState) blockState).get(BlockSlab.TYPE) == SlabType.BOTTOM;
            }
            return false;
        }

    }

}
