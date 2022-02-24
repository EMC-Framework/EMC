package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.TileBlockPosition;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

/**
 * @author Deftware
 */
public class TileEntity {

	private BoundingBox SINGLE;
	protected final net.minecraft.tileentity.TileEntity entity;
	protected final BlockPosition position;

	protected Block block;

	public static TileEntity newInstance(net.minecraft.tileentity.TileEntity entity) {
		if (entity instanceof TileEntityLockableLoot || entity instanceof TileEntityEnderChest) {
			return StorageEntity.newInstance(entity);
		}
		return new TileEntity(entity);
	}

	public BoundingBox getBoundingBox() {
		if (SINGLE == null) {
			SINGLE = getBlockPosition().getBoundingBox();
		}
		return SINGLE;
	}

	public net.minecraft.tileentity.TileEntity getMinecraftEntity() {
		return entity;
	}

	protected TileEntity(net.minecraft.tileentity.TileEntity entity) {
		this.entity = entity;
		this.position = new TileBlockPosition(entity);
		ResourceLocation identifier = net.minecraft.tileentity.TileEntity.getKey(entity.getClass());
		if (identifier != null) {
			Optional<Block> block = BlockRegistry.INSTANCE.find(
				identifier.getPath()
			);
			block.ifPresent(value -> this.block = value);
		}
	}

	public String getClassName() {
		return this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - "Entity".length());
	}

	public Block getBlock() {
		return block;
	}

	public BlockPosition getBlockPosition() {
		return position;
	}

	public float distanceTo(Entity entity) {
		return position.distanceTo(entity.getBlockPosition());
	}

}
