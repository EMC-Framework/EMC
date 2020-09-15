package me.deftware.client.framework.math.position;

import net.minecraft.util.BlockPos;

/**
 * @author Deftware
 */
public class TileBlockPosition extends BlockPosition {

	protected final net.minecraft.tileentity.TileEntity entity;

	public TileBlockPosition(net.minecraft.tileentity.TileEntity entity) {
		super(null);
		this.entity = entity;
	}

	@Override
	public double getX() {
		return entity.getPos().getX();
	}

	@Override
	public double getY() {
		return entity.getPos().getY();
	}

	@Override
	public double getZ() {
		return entity.getPos().getZ();
	}

	@Override
	public BlockPos getMinecraftBlockPos() {
		return entity.getPos();
	}

}
