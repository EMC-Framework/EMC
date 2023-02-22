package me.deftware.client.framework.math.position;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * @author Deftware
 */
public class DoubleBlockPosition extends BlockPosition {

	protected double x, y, z;

	public static BlockPosition fromMinecraftBlockPos(BlockPos blockPos) {
		return new DoubleBlockPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public DoubleBlockPosition(double x, double y, double z) {
		super(null);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getZ() {
		return z;
	}

	@Override
	public BlockPos getMinecraftBlockPos() {
		return fromDouble(x, y, z);
	}

	public static BlockPos fromDouble(double x, double y, double z) {
		return new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
	}

}
