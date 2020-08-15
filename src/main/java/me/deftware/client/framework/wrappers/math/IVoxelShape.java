package me.deftware.client.framework.wrappers.math;

// 1.12.2 and Below uses IAxisAlignedBB instead
public class IVoxelShape {

    private final IAxisAlignedBB shape;

    public IVoxelShape(IAxisAlignedBB shape) {
        this.shape = shape;
    }

    public IAxisAlignedBB getBoundingBox() {
        return new IAxisAlignedBB(shape.getAABB());
    }

    public IAxisAlignedBB getVoxelShape() {
        return shape;
    }

}