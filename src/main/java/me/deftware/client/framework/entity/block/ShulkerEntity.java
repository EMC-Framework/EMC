package me.deftware.client.framework.entity.block;

import net.minecraft.tileentity.TileEntityShulkerBox;

import java.awt.*;

/**
 * @author Deftware
 */
public class ShulkerEntity extends StorageEntity {

	private final Color color;

	public ShulkerEntity(net.minecraft.tileentity.TileEntity entity) {
		super(entity);
		color = new Color(getMinecraftEntity().getColor().getColorValue());
	}

	@Override
	public TileEntityShulkerBox getMinecraftEntity() {
		return (TileEntityShulkerBox) entity;
	}

	public Color getColor() {
		return color;
	}

}
