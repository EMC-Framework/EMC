package me.deftware.client.framework.wrappers.world;

import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import me.deftware.client.framework.wrappers.math.IVec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class IChunkPos {

    private double x, z;
    private IBlockPos centerPos;
    private IAxisAlignedBB boundingBox;
    private ChunkPos pos;

    public IChunkPos(ChunkPos pos) {
        this.pos = pos;
        updateCords(false);
    }

    public IChunkPos(BlockPos pos) {
        this.pos = new ChunkPos(pos);
        updateCords(false);
    }

    public IChunkPos(double x, double z) {
        this.x = x;
        this.z = z;
        updateCords(true);
    }

    public IChunkPos(IVec3d vec) {
        this.x = vec.vector.xCoord;
        this.z = vec.vector.zCoord;
        updateCords(true);
    }

    public ChunkPos getPos() {
        return pos;
    }

    public IBlockPos getBlockPos() {
        return centerPos;
    }

    public IAxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public double getX() {
        return x;
    }

    public double getStartX() {
        return pos.getXStart();
    }

    public double getEndX() {
        return pos.getXEnd();
    }

    public double getStartZ() {
        return pos.getZStart();
    }

    public double getEndZ() {
        return pos.getZEnd();
    }

    public void setX(double x) {
        this.x = x;
        updateCords(true);
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
        updateCords(true);
    }

    private void updateCords(boolean blockPos) {
        if (blockPos) {
            centerPos = new IBlockPos((int)x << 4, 0, (int)z << 4);
            pos = new ChunkPos(centerPos.getPos());
            boundingBox = new IAxisAlignedBB(getStartX(), 0, getStartZ(), getEndX(), 255, getEndZ());
        } else {
            boundingBox = new IAxisAlignedBB(getStartX(), 0, getStartZ(), getEndX(), 255, getEndZ());
            x = pos.chunkXPos;
            z = pos.chunkZPos;
            centerPos = new IBlockPos((int)x << 4, 0, (int)z << 4);
        }
    }

    public String toCords() {
        return pos.chunkXPos + ", " + pos.chunkZPos;
    }

    public boolean compareTo(IChunkPos pos2) {
        return pos.chunkXPos == pos2.getX() && pos.chunkZPos == pos2.getZ();
    }
}
