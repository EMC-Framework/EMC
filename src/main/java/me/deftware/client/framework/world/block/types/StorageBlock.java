package me.deftware.client.framework.world.block.types;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.world.block.BlockState;
import me.deftware.client.framework.world.block.InteractableBlock;
import net.minecraft.block.BlockChest;

/**
 * @author Deftware
 */
public class StorageBlock extends InteractableBlock {

	public static StorageBlock newInstance(net.minecraft.block.Block block) {
		if (block instanceof BlockChest) {
			return new me.deftware.client.framework.world.block.types.ChestBlock(block);
		}
		return new StorageBlock(block);
	}

	public BoundingBox getBoundingBox(BlockState state) {
		int x = getBlockPosition().getX();
		int y = getBlockPosition().getY();
		int z = getBlockPosition().getZ();
		return BoundingBox.of(x, y, z, x + 1, y + 1, z + 1);
	}
	
	protected StorageBlock(net.minecraft.block.Block block) {
		super(block);
	}

}
