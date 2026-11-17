package me.deftware.client.framework.entity.block;

import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;

/**
 * @author Deftware
 */
public class ChestEntity extends StorageEntity {

	public ChestEntity(BlockEntity entity, TickingBlockEntity ticker) {
		super(entity, ticker);
	}

	public boolean isFirst() {
		return !isEnderChest() && ChestBlock.getBlockType(entity.getBlockState()) == DoubleBlockCombiner.BlockType.FIRST;
	}

	public boolean isDouble() {
		return !isEnderChest() && ChestBlock.getBlockType(entity.getBlockState()) != DoubleBlockCombiner.BlockType.SINGLE;
	}

	public boolean isEnderChest() {
		return entity instanceof EnderChestBlockEntity;
	}

	public boolean isTrapped() {
		return entity instanceof TrappedChestBlockEntity;
	}

}
