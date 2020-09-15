package me.deftware.client.framework.world.block.types;

import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.math.box.DoubleBoundingBox;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.world.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;

/**
 * @author Deftware
 */
public class ChestBlock extends StorageBlock {

	public ChestBlock(Block block) {
		super(block);
	}

	@Override
	public BoundingBox getBoundingBox(BlockState state) {
		return getChestBoundingBox(isDouble(state), getBlockPosition(), state.getMinecraftBlockState());
	}
	
	public static BoundingBox getChestBoundingBox(boolean isDouble, BlockPosition position, IBlockState state) {
		if (isDouble) {
			switch (getChestShape(position.getMinecraftBlockPos())) {
				case NORTH:
					return new DoubleBoundingBox(position.getX(), position.getY(), position.getZ() - 1,
							position.getX() + 1.0, position.getY() + 1.0, position.getZ() + 1.0);
				case SOUTH:
					return new DoubleBoundingBox(position.getX(), position.getY(), position.getZ(),
							position.getX() + 1.0, position.getY() + 1.0, position.getZ() + 2.0);
				case WEST:
					return new DoubleBoundingBox(position.getX() - 1, position.getY(), position.getZ(),
							position.getX() + 1.0, position.getY() + 1.0, position.getZ() + 1.0);
				case EAST:
					return new DoubleBoundingBox(position.getX(), position.getY(), position.getZ(),
							position.getX() + 2.0, position.getY() + 1.0, position.getZ() + 1.0);
			}
		}
		return position.getBoundingBox();
	}

	public boolean isFirst(BlockState state) {
		if (state.pos == null) return false;
		return isFirstChest(state.pos);
	}

	public boolean isDouble(BlockState state) {
		if (state.pos == null) return false;
		return getChestShape(state.pos) != ChestShape.SINGLE;
	}

	public static boolean isFirstChest(BlockPos pos) {
		ChestShape shape = getChestShape(pos);
		if (shape == ChestShape.SINGLE) return false;
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

	public static boolean isChest(BlockPos pos) {
		Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
		return block instanceof BlockChest;
	}

	public static ChestShape getChestShape(BlockPos pos) {
		WorldClient world = Minecraft.getMinecraft().theWorld;
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

	public static enum ChestShape {
		NORTH, SOUTH, EAST, WEST, SINGLE
	}

}
