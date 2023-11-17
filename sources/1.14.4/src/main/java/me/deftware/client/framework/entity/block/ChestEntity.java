package me.deftware.client.framework.entity.block;

import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.block.enums.ChestType;

/**
 * @author Deftware
 */
public class ChestEntity extends StorageEntity {

	public ChestEntity(BlockEntity entity) {
		super(entity);
	}

	public boolean isFirst() {
		return !isEnderChest() && entity.getCachedState().get(ChestBlock.CHEST_TYPE) == ChestType.LEFT;
	}

	public boolean isDouble() {
		return !isEnderChest() && entity.getCachedState().get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE;
	}

	public boolean isEnderChest() {
		return entity instanceof EnderChestBlockEntity;
	}

	public boolean isTrapped() {
		return entity instanceof TrappedChestBlockEntity;
	}

}
