package me.deftware.client.framework.entity.block;

import java.awt.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

/**
 * @author Deftware
 */
public class ShulkerEntity extends StorageEntity {

	private Color color;

	public ShulkerEntity(BlockEntity entity, TickingBlockEntity ticker) {
		super(entity, ticker);
	}

	@Override
	public ShulkerBoxBlockEntity getMinecraftEntity() {
		return (ShulkerBoxBlockEntity) entity;
	}

	public Color getColor() {
		if (color == null) {
			if (getMinecraftEntity().getColor() == null) {
				color = Color.pink;
			} else {
				color = new Color(getMinecraftEntity().getColor().getTextureDiffuseColor());
			}
		}
		return color;
	}

}
