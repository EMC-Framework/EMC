package me.deftware.client.framework.math;

import me.deftware.client.framework.world.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author Deftware
 */
public interface BlockPosition extends Vector3<Integer> {

    long asLong();

    BlockPosition offset(EnumFacing direction);

    default BlockPosition offset(int x, int y, int z) {
        return of(getX() + x, getY() + y, getZ() + z);
    }

    static BlockPosition of(int x, int y, int z) {
        return (BlockPosition) new BlockPos(x, y, z);
    }

    static BlockPosition of(Vector3<Integer> vector) {
        return of(vector.getX(), vector.getY(), vector.getZ());
    }

}
