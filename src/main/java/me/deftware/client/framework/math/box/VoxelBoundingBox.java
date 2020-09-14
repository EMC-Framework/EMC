package me.deftware.client.framework.math.box;

import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author Deftware
 */
public class VoxelBoundingBox extends BoundingBox {

	protected final AxisAlignedBB voxelShape;

	public VoxelBoundingBox(AxisAlignedBB AxisAlignedBB) {
		super(null);
		this.voxelShape = AxisAlignedBB;
	}

	@Override
	public AxisAlignedBB getMinecraftBox() {
		return voxelShape;
	}

}
