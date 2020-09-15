package me.deftware.client.framework.math.box;

import me.deftware.client.framework.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;

import javax.annotation.Nullable;

/**
 * @author Deftware
 */
public class BoundingBox {

	protected final Entity entity;
	public double offsetX = 0, offsetY = 0, offsetZ = 0;

	public BoundingBox(Entity entity) {
		this.entity = entity;
	}

	public net.minecraft.util.math.AxisAlignedBB getMinecraftBox() {
		return entity.getEntityBoundingBox();
	}

	public net.minecraft.util.math.AxisAlignedBB getOffsetMinecraftBox() {
		return getOffsetMinecraftBox(offsetX, offsetY, offsetZ);
	}

	public net.minecraft.util.math.AxisAlignedBB getOffsetMinecraftBox(double offsetX, double offsetY, double offsetZ) {
		return getMinecraftBox().offset(offsetX, offsetY, offsetZ);
	}

	public BoundingBox offset(double offsetX, double offsetY, double offsetZ) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		return this;
	}

	public long asLong() {
		return new BlockPos(getMinX(), getMinY(), getMinZ()).toLong(); // Kinda hack
	}
	
	public float squareDistanceTo(me.deftware.client.framework.entity.Entity entity) {
		float f = (float)(this.getMinX() - entity.getPosX());
		float g = (float)(this.getMinY() - entity.getPosY());
		float h = (float)(this.getMinZ() - entity.getPosZ());
		return MathHelper.sqrt(f * f + g * g + h * h);
	}

	@Override
	public String toString() {
		return getMinX() + ", " + getMinY() + ", " + getMinZ() + ", " + getMaxX() + ", " + getMaxY() + ", " + getMaxZ();
	}

	public Vector3d getCenter() {
		return new Vector3d(getMinecraftBox().getCenter());
	}

	public double getMinX() {
		return getMinecraftBox().minX + offsetX;
	}

	public double getMinY() {
		return getMinecraftBox().minY + offsetY;
	}

	public double getMinZ() {
		return getMinecraftBox().minZ + offsetZ;
	}

	public double getMaxX() {
		return getMinecraftBox().maxX + offsetX;
	}

	public double getMaxY() {
		return getMinecraftBox().maxY + offsetY;
	}

	public double getMaxZ() {
		return getMinecraftBox().maxZ + offsetZ;
	}

	@Nullable
	public Vector3d rayTrace(Vector3d min, Vector3d max) {
		RayTraceResult vec3d = getMinecraftBox().calculateIntercept(min.getMinecraftVector(), max.getMinecraftVector());
		return vec3d == null ? null : new Vector3d(vec3d.hitVec);
	}

}
