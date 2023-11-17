package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.entity.Entity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityLockable;
import me.deftware.client.framework.world.block.Block;

/**
 * @author Deftware
 */
public class TileEntity {

	protected final net.minecraft.tileentity.TileEntity entity;

	protected Block block;

	public static TileEntity newInstance(net.minecraft.tileentity.TileEntity entity) {
		if (entity instanceof TileEntityLockable || entity instanceof TileEntityEnderChest) {
			return StorageEntity.newInstance(entity);
		}
		return new TileEntity(entity);
	}

	public net.minecraft.tileentity.TileEntity getMinecraftEntity() {
		return entity;
	}

	protected TileEntity(net.minecraft.tileentity.TileEntity entity) {
		this.entity = entity;
	}

	public String getClassName() {
		return this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - "Entity".length());
	}

	public synchronized Block getBlock() {
		if (block == null) {
			if (entity.getBlockType() != null) {
				this.block = (Block) entity.getBlockType();
			}
		}
		return block;
	}

	public BlockPosition getBlockPosition() {
		return (BlockPosition) getMinecraftEntity().getPos();
	}

	public double distanceTo(Entity entity) {
		return getBlockPosition().distanceTo(entity.getBlockPosition());
	}

}
