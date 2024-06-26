package me.deftware.client.framework.entity.block;

import lombok.Getter;
import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.BlockEntityTickInvoker;

import java.util.Optional;

/**
 * @author Deftware
 */
public class TileEntity {

	protected final BlockEntity entity;

	@Getter
	protected final BlockEntityTickInvoker ticker;

	protected Block block;

	public static TileEntity newInstance(BlockEntity entity, BlockEntityTickInvoker ticker) {
		if (entity instanceof LootableContainerBlockEntity || entity instanceof EnderChestBlockEntity) {
			return StorageEntity.newInstance(entity, ticker);
		}
		return new TileEntity(entity, ticker);
	}

	public BlockEntity getMinecraftEntity() {
		return entity;
	}

	protected TileEntity(BlockEntity entity, BlockEntityTickInvoker ticker) {
		this.ticker = ticker;
		this.entity = entity;
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

	public BlockPosition getBlockPosition() {
		return (BlockPosition) getMinecraftEntity().getPos();
	}

	public double distanceTo(Entity entity) {
		return getBlockPosition().distanceTo(entity.getBlockPosition());
	}

	public Block getBlock() {
		return block;
	}

}
