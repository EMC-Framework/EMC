package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.world.block.BlockUtils;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.math.BlockPos;

/**
 * @author Deftware
 */
public class ChestEntity extends StorageEntity {

	public ChestEntity(net.minecraft.tileentity.TileEntity entity) {
		super(entity);
	}

	public boolean isFirst() {
		return BlockUtils.isFirstChest((BlockPos) getBlockPosition());
	}

	public boolean isDouble() {
		return BlockUtils.getChestShape((BlockPos) getBlockPosition()) != BlockUtils.ChestShape.SINGLE;
	}

	public boolean isEnderChest() {
		return entity instanceof TileEntityEnderChest;
	}

	public boolean isTrapped() {
		return entity instanceof TileEntityChest && ((TileEntityChest) entity).getChestType() == BlockChest.Type.TRAP;
	}

}
