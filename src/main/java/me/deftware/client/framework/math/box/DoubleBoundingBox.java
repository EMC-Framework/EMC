package me.deftware.client.framework.math.box;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * @author Deftware
 */
public class DoubleBoundingBox extends BoundingBox {

	protected AxisAlignedBB box;
	public long asLong = 0L;

	public DoubleBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		super(null);
		this.box = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public DoubleBoundingBox(BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
		asLong = pos.toLong();
	}

	public DoubleBoundingBox(AxisAlignedBB box) {
		super(null);
		this.box = box;
	}

	@Override
	public AxisAlignedBB getMinecraftBox() {
		return box;
	}

}
