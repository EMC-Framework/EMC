package me.deftware.client.framework.math.box;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author Deftware
 */
public class VoxelShape {

	public static final VoxelShape SOLID = new VoxelShape(Block.FULL_BLOCK_AABB),
			EMPTY = new VoxelShape(Block.NULL_AABB);

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
