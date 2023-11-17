package me.deftware.client.framework.math;

import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShapes;

/**
 * @author Deftware
 */
public interface Voxel {

    BoundingBox getBoundingBox();

    static Voxel cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return (Voxel) Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ);
    }

    static Voxel solid() {
        return (Voxel) VoxelShapes.fullCube();
    }

    static Voxel empty() {
        return (Voxel) VoxelShapes.empty();
    }

}
