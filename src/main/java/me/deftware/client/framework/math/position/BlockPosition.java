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

	private static final int SIZE_BITS_X = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
	private static final int SIZE_BITS_Z = SIZE_BITS_X;
	private static final int SIZE_BITS_Y = 64 - SIZE_BITS_X - SIZE_BITS_Z;
	private static final long BITS_X = (1L << SIZE_BITS_X) - 1L;
	private static final long BITS_Y = (1L << SIZE_BITS_Y) - 1L;
	private static final long BITS_Z= (1L << SIZE_BITS_Z) - 1L;
	private static final int BIT_SHIFT_Z = SIZE_BITS_Y;
	private static final int BIT_SHIFT_X = SIZE_BITS_Y + SIZE_BITS_Z;

	protected final Entity entity;

	public BlockPosition(Entity entity) {
		this.entity = entity;
	}

	public double getX() {
		return entity.posX;
	}

	public double getY() {
		return entity.posY;
	}

	public double getZ() {
		return entity.posZ;
	}

	public BlockPos getMinecraftBlockPos() {
		return entity.getPosition();
	}

	public BoundingBox getEntityBoundingBox() {
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

	public static int unpackLongX(long l) {
		return (int)(l << 64 - BIT_SHIFT_X - SIZE_BITS_X >> 64 - SIZE_BITS_X);
	}

	public static int unpackLongY(long l) {
		return (int)(l << 64 - SIZE_BITS_Y >> 64 - SIZE_BITS_Y);
	}

	public static int unpackLongZ(long l) {
		return (int)(l << 64 - BIT_SHIFT_Z - SIZE_BITS_Z >> 64 - SIZE_BITS_Z);
	}

	public long asLong() {
		return getMinecraftBlockPos().toLong();
	}

	@Override
	public String toString() {
		return getX() + ", " + getY() + ", " + getZ();
	}

}
