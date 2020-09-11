package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.world.block.types.ChestBlock;
import net.minecraft.block.BlockChest;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityTrappedChest;

/**
 * @author Deftware
 */
public class ChestEntity extends StorageEntity {

	public ChestEntity(net.minecraft.tileentity.TileEntity entity) {
		super(entity);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return me.deftware.client.framework.world.block.types.ChestBlock.getChestBoundingBox(isDouble(), getBlockPosition(), entity.getBlockState());
	}

	public boolean isFirst() {
		return !isEnderChest() && entity.getBlockState().get(BlockChest.TYPE) == ChestType.LEFT;
	}

	public boolean isDouble() {
		return !isEnderChest() && entity.getBlockState().get(BlockChest.TYPE) != ChestType.SINGLE;
	}

	public boolean isEnderChest() {
		return entity instanceof TileEntityEnderChest;
	}

	public boolean isTrapped() {
		return entity instanceof TileEntityTrappedChest;
	}

}
