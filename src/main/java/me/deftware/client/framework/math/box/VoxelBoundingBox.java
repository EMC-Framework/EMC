package me.deftware.client.framework.math.box;

import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author Deftware
 */
public class VoxelBoundingBox extends BoundingBox {

	protected final net.minecraft.util.math.shapes.VoxelShape voxelShape;

	public VoxelBoundingBox(net.minecraft.util.math.shapes.VoxelShape voxelShape) {
		super(null);
		this.voxelShape = voxelShape;
	}

	@Override
	public AxisAlignedBB getMinecraftBox() {
		return voxelShape.getBoundingBox();
	}

}
