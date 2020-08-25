package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.TileBlockPosition;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;

/**
 * @author Deftware
 */
public class TileEntity {

	protected final BlockEntity entity;
	protected final BlockPosition position;

	@SuppressWarnings("DuplicateCondition")
	public static TileEntity newInstance(BlockEntity entity) {
		if (entity instanceof LootableContainerBlockEntity || entity instanceof EnderChestBlockEntity) {
			return StorageEntity.newInstance(entity);
		}
		return new TileEntity(entity);
	}

	protected TileEntity(BlockEntity entity) {
		this.entity = entity;
		this.position = new TileBlockPosition(entity);
	}
	
	

	public BlockPosition getBlockPosition() {
		return position;
	}

	public float distanceTo(Entity entity) {
		return position.distanceTo(entity.getBlockPosition());
	}

}
