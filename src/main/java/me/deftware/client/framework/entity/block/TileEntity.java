package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.BoundingBox;
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

	public BoundingBox getBoundingBox() {
		int x = entity.getPos().getX();
		int y = entity.getPos().getY();
		int z = entity.getPos().getZ();
		return BoundingBox.of(x, y, z, x + 1, y + 1, z + 1);
	}

	public net.minecraft.tileentity.TileEntity getMinecraftEntity() {
		return entity;
	}

	protected TileEntity(net.minecraft.tileentity.TileEntity entity) {
		this.entity = entity;
		/*if (entity.getBlockType() != null) {
			this.block = BlockRegistry.INSTANCE.getBlock(entity.getBlockType());
		}*/
	}

	public String getClassName() {
		return this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - "Entity".length());
	}

	public Block getBlock() {
		return block;
	}

	public BlockPosition getBlockPosition() {
		return (BlockPosition) getMinecraftEntity().getPos();
	}

	public double distanceTo(Entity entity) {
		return getBlockPosition().distanceTo(entity.getBlockPosition());
	}

}
