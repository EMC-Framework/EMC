package me.deftware.client.framework.math.vector;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.math.position.BlockPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * @author Deftware
 */
public class Vector3d {

	public static final Vector3d ZERO = new Vector3d(0.0D, 0.0D, 0.0D);

	protected Vec3d vec3d;

	public Vector3d(double x, double y, double z) {
		vec3d = new Vec3d(x, y, z);
	}

	public Vector3d(Vec3i vec) {
		vec3d = new Vec3d(vec.getX(), vec.getY(), vec.getZ());
	}

	public Vector3d(Vec3d vec) {
		this.vec3d = vec;
	}

	public Vector3d(BlockPosition position) {
		this(position.getX(), position.getY(), position.getZ());
	}

	public Vector3d(Entity entity) {
		this(entity.getBlockPosition());
	}

	public Vector3d(TileEntity entity) {
		this(entity.getBlockPosition());
	}

	public Vec3d getMinecraftVector() {
		return vec3d;
	}

	public double getX() {
		return vec3d.x;
	}

	public double getY() {
		return vec3d.y;
	}

	public double getZ() {
		return vec3d.z;
	}

	public Vector3d multiply(double factor) {
		return new Vector3d(vec3d.scale(factor));
	}

	public Vector3d subtract(double x, double y, double z) {
		return new Vector3d(vec3d.subtract(x, y, z));
	}

	public Vector3d add(double x, double y, double z) {
		return new Vector3d(vec3d.add(x, y, z));
	}

	public Vector3d set(double x, double y, double z) {
		if (x == 0) x = getX();
		if (y == 0) y = getY();
		if (z == 0) z = getZ();
		return new Vector3d(x, y, z);
	}

	public Vector3d subtract(Vector3d vec) {
		return new Vector3d(vec3d.subtract(vec.getMinecraftVector()));
	}

	public Vector3d add(Vector3d vec) {
		return new Vector3d(vec3d.add(vec.getMinecraftVector()));
	}

	public Vector3d floor() {
		return new Vector3d(
			Math.floor(getX()), Math.floor(getY()), Math.floor(getZ())
		);
	}

	public double squareDistanceTo(Vector3d vec) {
		return vec3d.distanceTo(vec.getMinecraftVector());
	}

	public static boolean rayTraceBlocks(Vector3d vec1, Vector3d vec2) {
		RayTraceResult hitResult_1 = Minecraft.getMinecraft().world.rayTraceBlocks(vec1.getMinecraftVector(), vec2.getMinecraftVector());
		return hitResult_1 != null && hitResult_1.typeOfHit != RayTraceResult.Type.MISS;
	}

	public double getMagnitude() {
		return Math.sqrt(vec3d.x * vec3d.x + vec3d.y * vec3d.y + vec3d.z * vec3d.z);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Vector3d) {
			Vector3d vec = (Vector3d) o;
			return vec.getX() == getX() && vec.getY() == vec.getY() && vec.getZ() == getZ();
		}
		return false;
	}

}
