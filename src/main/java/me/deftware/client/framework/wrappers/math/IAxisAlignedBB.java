package me.deftware.client.framework.wrappers.math;

import net.minecraft.util.math.BoundingBox;

public class IAxisAlignedBB {

    public final double x1;
    public final double y1;
    public final double z1;
    public final double x2;
    public final double y2;
    public final double z2;

    private BoundingBox bb;

    public IAxisAlignedBB(BoundingBox aabb) {
        bb = aabb;
        x1 = bb.minX;
        y1 = bb.minY;
        z1 = bb.minZ;
        x2 = bb.maxX;
        y2 = bb.maxY;
        z2 = bb.maxZ;
    }

    public IAxisAlignedBB(double x, double y, double z, double x1, double y1, double z1) {
        bb = new BoundingBox(x, y, z, x1, y1, z1);
        this.x1 = bb.minX;
        this.y1 = bb.minY;
        this.z1 = bb.minZ;
        x2 = bb.maxX;
        y2 = bb.maxY;
        z2 = bb.maxZ;
    }

    public BoundingBox getAABB() {
        return bb;
    }

    public IAxisAlignedBB offSet(double x, double y, double z) {
        return new IAxisAlignedBB(bb.offset(x, y, z));
    }

}
