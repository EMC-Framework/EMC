package me.deftware.client.framework.entity.block;

import lombok.Getter;
import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.registry.BlockRegistry;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import java.util.Optional;

/**
 * @author Deftware
 */
public class TileEntity {

	protected final BlockEntity entity;

	@Getter
	protected final TickingBlockEntity ticker;

	protected Block block;

	public static TileEntity newInstance(BlockEntity entity, TickingBlockEntity ticker) {
		if (entity instanceof RandomizableContainerBlockEntity || entity instanceof EnderChestBlockEntity) {
			return StorageEntity.newInstance(entity, ticker);
		}
		return new TileEntity(entity, ticker);
	}

	public BlockEntity getMinecraftEntity() {
		return entity;
	}

	protected TileEntity(BlockEntity entity, TickingBlockEntity ticker) {
		this.ticker = ticker;
		this.entity = entity;
		Identifier identifier = net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType());
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
		return (BlockPosition) getMinecraftEntity().getBlockPos();
	}

	public double distanceTo(Entity entity) {
		return getBlockPosition().distanceTo(entity.getBlockPosition());
	}

	public Block getBlock() {
		return block;
	}

}
