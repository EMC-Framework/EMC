package me.deftware.client.framework.math;

/**
 * @author Deftware
 */
public interface Voxel {

    BoundingBox getBoundingBox();

    static Voxel cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return (Voxel) BoundingBox.of(minX, minY, minZ, maxX, maxY, maxZ);
    }

    static Voxel solid() {
        return (Voxel) BoundingBox.of(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    }

    static Voxel empty() {
        return (Voxel) BoundingBox.of(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

}
