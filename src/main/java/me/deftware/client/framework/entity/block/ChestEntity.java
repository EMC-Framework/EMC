package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.math.box.BoundingBox;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

/**
 * @author Deftware
 */
public class ChestEntity extends StorageEntity {

	public ChestEntity(net.minecraft.tileentity.TileEntity entity) {
		super(entity);
	}

	@Override
	public BoundingBox getEntityBoundingBox() {
		return me.deftware.client.framework.world.block.types.ChestBlock.getChestBoundingBox(isDouble(), getBlockPosition(), null);
	}

	public boolean isFirst() {
		return false; //return !isEnderChest() && entity.getBlockState().get(BlockChest.TYPE) == ChestType.LEFT;
	}

	public boolean isDouble() {
		return false; //return !isEnderChest() && entity.getBlockState().get(BlockChest.TYPE) != ChestType.SINGLE;
	}

	public boolean isEnderChest() {
		return entity instanceof TileEntityEnderChest;
	}

	public boolean isTrapped() {
		return entity instanceof TileEntityChest && ((TileEntityChest) entity).getChestType() == BlockChest.Type.TRAP;
	}

}
