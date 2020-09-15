package me.deftware.client.framework.entity.block;

import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityHopper;

/**
 * @author Deftware
 */
public class StorageEntity extends TileEntity {

	public static StorageEntity newInstance(net.minecraft.tileentity.TileEntity entity) {
		if (entity instanceof TileEntityChest || entity instanceof TileEntityEnderChest) {
			return new ChestEntity(entity);
		} else if (entity instanceof TileEntityHopper) {
			return new HopperEntity(entity);
		}
		return new StorageEntity(entity);
	}

	protected StorageEntity(net.minecraft.tileentity.TileEntity entity) {
		super(entity);
	}

}
