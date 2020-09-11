package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.math.box.BoundingBox;
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

	@Override
	public BoundingBox getBoundingBox() {
		return me.deftware.client.framework.world.block.types.ChestBlock.getChestBoundingBox(isDouble(), getBlockPosition(), entity.getCachedState());
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
