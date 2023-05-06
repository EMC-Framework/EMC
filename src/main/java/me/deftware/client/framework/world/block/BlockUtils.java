package me.deftware.client.framework.world.block;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.world.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.*;

public class BlockUtils {

    public static DoubleBlockType getDoubleBlockType(BlockPosition position) {
        BlockPos pos = (BlockPos) position;
        IBlockState state = Minecraft.getMinecraft().world.getBlockState(pos);
        if (state.getBlock() instanceof BlockChest) {
            ChestShape shape = getChestShape(pos);
            if (shape != ChestShape.SINGLE) {
                if (isFirstChest(pos)) {
                    return DoubleBlockType.First;
                }
                return DoubleBlockType.Second;
            }
        }
        return DoubleBlockType.Single;
    }

    public static EnumFacing getBlockFacing(BlockPosition position) {
        IBlockState state = Minecraft.getMinecraft().world.getBlockState((BlockPos) position);
        if (state.getBlock() instanceof BlockChest) {
            return EnumFacing.fromMinecraft(state.getValue(BlockChest.FACING));
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
            return false;
        }

        public static boolean isFluidWater(BlockState blockState) {
            return false;
        }

        public static boolean isFluidFlowing(BlockState blockState) {
            return false;
        }


    }

    public static class State {

        public static int getSnowLayers(BlockState blockState) {
            return 0;
        }

        public static boolean isBottomSlab(BlockState blockState) {
            return false;
        }

    }

    /*
        Helper methods not available in <= 1.12.2
     */

    public static boolean isChest(BlockPos pos) {
        return Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockChest;
    }

    public static boolean isFirstChest(BlockPos pos) {
        ChestShape shape = getChestShape(pos);
        if (shape == ChestShape.SINGLE) {
            return false;
        }
        switch (getChestShape(pos)) {
            case NORTH:
                return isChest(pos.north());
            case SOUTH:
                return isChest(pos.south());
            case WEST:
                return isChest(pos.west());
            default: // EAST
                return isChest(pos.east());
        }
    }

    public static ChestShape getChestShape(BlockPos pos) {
        WorldClient world = Minecraft.getMinecraft().world;
        Block block = world.getBlockState(pos).getBlock();
        if (world.getBlockState(pos.north()).getBlock() == block) {
            return ChestShape.NORTH;
        } else if (world.getBlockState(pos.south()).getBlock() == block) {
            return ChestShape.SOUTH;
        } else if (world.getBlockState(pos.west()).getBlock() == block) {
            return ChestShape.WEST;
        } else {
            return world.getBlockState(pos.east()).getBlock() == block ? ChestShape.EAST : ChestShape.SINGLE;
        }
    }

    public enum ChestShape {
        NORTH, SOUTH, EAST, WEST, SINGLE
    }

}
