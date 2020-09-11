package me.deftware.client.framework.world.block.types;

import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.math.box.DoubleBoundingBox;
import me.deftware.client.framework.math.position.BlockPosition;
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
		return state.getMinecraftBlockState().get(BlockChest.TYPE) == ChestType.LEFT;
	}

	public boolean isDouble(BlockState state) {
		return state.getMinecraftBlockState().get(BlockChest.TYPE) != ChestType.SINGLE;
	}

}
