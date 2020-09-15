package me.deftware.client.framework.math.box;

import net.minecraft.util.AxisAlignedBB;

/**
 * @author Deftware
 */
public class VoxelShape {

	public static final VoxelShape SOLID = new VoxelShape(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)),
			EMPTY = new VoxelShape(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D));

	private final AxisAlignedBB shape;
	private final VoxelBoundingBox boundingBox;

	public VoxelShape(AxisAlignedBB shape) {
		this.shape = shape;
		this.boundingBox = new VoxelBoundingBox(shape);
	}

	public VoxelBoundingBox getBoundingBox() {
		return boundingBox;
	}

	public AxisAlignedBB getMinecraftVoxelShape() {
		return shape;
	}

	public static VoxelShape makeCuboidShape(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		return new VoxelShape(new DoubleBoundingBox(minX, minY, minZ, maxX, maxY, maxZ).getMinecraftBox());
	}

}
