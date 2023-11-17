package me.deftware.client.framework.math;

import net.minecraft.block.Block;

/**
 * @author Deftware
 */
public interface Voxel {

    BoundingBox getBoundingBox();

    static Voxel cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return (Voxel) BoundingBox.of(minX, minY, minZ, maxX, maxY, maxZ);
    }

    static Voxel solid() {
        return (Voxel) Block.FULL_BLOCK_AABB;
    }

    static Voxel empty() {
        return (Voxel) Block.NULL_AABB;
    }

}
