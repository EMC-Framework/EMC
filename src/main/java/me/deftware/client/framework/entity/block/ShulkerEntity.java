package me.deftware.client.framework.entity.block;

import net.minecraft.tileentity.TileEntityShulkerBox;

import java.awt.*;

/**
 * @author Deftware
 */
public class ShulkerEntity extends StorageEntity {

	private Color color;

	public ShulkerEntity(net.minecraft.tileentity.TileEntity entity) {
		super(entity);
	}

	@Override
	public TileEntityShulkerBox getMinecraftEntity() {
		return (TileEntityShulkerBox) entity;
	}

	public Color getColor() {
		if (color == null) {
			color = new Color(getMinecraftEntity().getColor().getColorValue());
		}
		return color;
	}

}
