package me.deftware.client.framework.entity.block;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.TileBlockPosition;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * @author Deftware
 */
public class TileEntity {

	private BoundingBox SINGLE;
	protected final BlockEntity entity;
	protected final BlockPosition position;

	protected Block block;

	public static TileEntity newInstance(BlockEntity entity) {
		if (entity instanceof LootableContainerBlockEntity || entity instanceof EnderChestBlockEntity) {
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

	public BlockEntity getMinecraftEntity() {
		return entity;
	}

	protected TileEntity(BlockEntity entity) {
		this.entity = entity;
		this.position = new TileBlockPosition(entity);
		Identifier identifier = BlockEntityType.getId(entity.getType());
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
