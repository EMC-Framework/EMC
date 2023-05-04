package me.deftware.client.framework.entity.block;

import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;

/**
 * @author Deftware
 */
public class ChestEntity extends StorageEntity {

	public ChestEntity(BlockEntity entity) {
		super(entity);
	}

	public boolean isFirst() {
		return !isEnderChest() && ChestBlock.getDoubleBlockType(entity.getCachedState()) == DoubleBlockProperties.Type.FIRST;
	}

	public boolean isDouble() {
		return !isEnderChest() && ChestBlock.getDoubleBlockType(entity.getCachedState()) != DoubleBlockProperties.Type.SINGLE;
	}

	public boolean isEnderChest() {
		return entity instanceof EnderChestBlockEntity;
	}

	public boolean isTrapped() {
		return entity instanceof TrappedChestBlockEntity;
	}

}
