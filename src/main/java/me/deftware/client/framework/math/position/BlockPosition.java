package me.deftware.client.framework.math.position;

import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.math.box.DoubleBoundingBox;
import me.deftware.client.framework.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * @author Deftware
 */
public class BlockPosition {

	protected final Entity entity;

	public BlockPosition(Entity entity) {
		this.entity = entity;
	}

	public double getX() {
		return entity.x;
	}

	public double getY() {
		return entity.y;
	}

	public double getZ() {
		return entity.z;
	}

	public BlockPos getMinecraftBlockPos() {
		return entity.getBlockPos();
	}

	public BoundingBox getBoundingBox() {
		return new DoubleBoundingBox(getX(), getY(), getZ(), getX() + 1.0, getY() + 1.0, getZ() + 1.0);
	}

	public BlockPosition offset(int x, int y, int z) {
		return new DoubleBlockPosition(getX() + x, getY() + y, getZ() + z);
	}

	public Vector3d getVector() {
		return new Vector3d(getX(), getY(), getZ());
	}

	public float distanceTo(BlockPosition position) {
		float deltaX = (float) (getX() - position.getX());
		float deltaY = (float) (getY() - position.getY());
		float deltaZ = (float) (getZ() - position.getZ());
		return MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}

	public static int unpackLongX(long x) {
		return BlockPos.unpackLongX(x);
	}

	public static int unpackLongY(long y) {
		return BlockPos.unpackLongY(y);
	}

	public static int unpackLongZ(long z) {
		return BlockPos.unpackLongZ(z);
	}

	public long asLong() {
		return getMinecraftBlockPos().asLong();
	}

	@Override
	public String toString() {
		return getX() + ", " + getY() + ", " + getZ();
	}

}
