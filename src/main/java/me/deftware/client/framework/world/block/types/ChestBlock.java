package me.deftware.client.framework.world.block.types;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.world.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.properties.ChestType;

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
			switch (BlockChest.getDirectionToAttached(state)) {
				case NORTH:
					return BoundingBox.of(position.getX(), position.getY(), position.getZ() - 1,
							position.getX() + 1, position.getY() + 1, position.getZ() + 1);
				case SOUTH:
					return BoundingBox.of(position.getX(), position.getY(), position.getZ(),
							position.getX() + 1, position.getY() + 1, position.getZ() + 2);
				case WEST:
					return BoundingBox.of(position.getX() - 1, position.getY(), position.getZ(),
							position.getX() + 1, position.getY() + 1, position.getZ() + 1);
				case EAST:
					return BoundingBox.of(position.getX(), position.getY(), position.getZ(),
							position.getX() + 2, position.getY() + 1, position.getZ() + 1);
			}
		}
		return BoundingBox.of(position.getX(), position.getY(), position.getZ(),
				position.getX() + 1, position.getY() + 1, position.getZ() + 1);
	}

	public boolean isFirst(BlockState state) {
		return state.getMinecraftBlockState().get(BlockChest.TYPE) == ChestType.LEFT;
	}

	public boolean isDouble(BlockState state) {
		return state.getMinecraftBlockState().get(BlockChest.TYPE) != ChestType.SINGLE;
	}

}
