package me.deftware.client.framework.world.block.types;

import me.deftware.client.framework.math.box.BoundingBox;
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

	public BoundingBox getEntityBoundingBox(BlockState state) {
		return getBlockPosition().getEntityBoundingBox();
	}
	
	protected StorageBlock(net.minecraft.block.Block block) {
		super(block);
	}

}
