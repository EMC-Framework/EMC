package me.deftware.client.framework.entity.block;

import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

/**
 * @author Deftware
 */
public class StorageEntity extends TileEntity {

	public static StorageEntity newInstance(BlockEntity entity, TickingBlockEntity ticker) {
		if (entity instanceof ChestBlockEntity || entity instanceof EnderChestBlockEntity) {
			return new ChestEntity(entity, ticker);
		} else if (entity instanceof BarrelBlockEntity) {
			return new BarrelEntity(entity, ticker);
		} else if (entity instanceof ShulkerBoxBlockEntity) {
			return new ShulkerEntity(entity, ticker);
		} else if (entity instanceof HopperBlockEntity) {
			return new HopperEntity(entity, ticker);
		}
		return new StorageEntity(entity, ticker);
	}

	protected StorageEntity(BlockEntity entity, TickingBlockEntity ticker) {
		super(entity, ticker);
	}

}
