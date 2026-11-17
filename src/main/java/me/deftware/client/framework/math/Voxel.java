package me.deftware.client.framework.math;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;

/**
 * @author Deftware
 */
public interface Voxel {

    BoundingBox getBoundingBox();

    static Voxel cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return (Voxel) Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    static Voxel solid() {
        return (Voxel) Shapes.block();
    }

    static Voxel empty() {
        return (Voxel) Shapes.empty();
    }

}
