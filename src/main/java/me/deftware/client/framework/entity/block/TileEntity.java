package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.TileBlockPosition;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityLockableLoot;

/**
 * @author Deftware
 */
public class TileEntity {

	protected final BoundingBox SINGLE;
	protected final net.minecraft.tileentity.TileEntity entity;
	protected final BlockPosition position;

	public static TileEntity newInstance(net.minecraft.tileentity.TileEntity entity) {
		if (entity instanceof TileEntityLockableLoot || entity instanceof TileEntityEnderChest) {
			return StorageEntity.newInstance(entity);
		}
		return new TileEntity(entity);
	}

	public BoundingBox getEntityBoundingBox() {
		return SINGLE;
	}

	public net.minecraft.tileentity.TileEntity getMinecraftEntity() {
		return entity;
	}

	protected TileEntity(net.minecraft.tileentity.TileEntity entity) {
		this.entity = entity;
		this.position = new TileBlockPosition(entity);
		SINGLE = getBlockPosition().getEntityBoundingBox();
	}
	
	public String getClassName() {
		return this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - "Entity".length());
	}

	public BlockPosition getBlockPosition() {
		return position;
	}

	public float distanceTo(Entity entity) {
		return position.distanceTo(entity.getBlockPosition());
	}

}
