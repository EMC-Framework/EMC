package me.deftware.client.framework.world;

import me.deftware.client.framework.math.vector.Vector3d;

/**
 * @author Deftware
 */
public enum EnumFacing {

	DOWN(net.minecraft.util.EnumFacing.DOWN), UP(net.minecraft.util.EnumFacing.UP), NORTH(net.minecraft.util.EnumFacing.NORTH), SOUTH(net.minecraft.util.EnumFacing.SOUTH), WEST(net.minecraft.util.EnumFacing.WEST), EAST(net.minecraft.util.EnumFacing.EAST);

	private final net.minecraft.util.EnumFacing direction;
	private final Vector3d vector3d;

	EnumFacing(net.minecraft.util.EnumFacing direction) {
		this.direction = direction;
		this.vector3d = new Vector3d(direction.getDirectionVec());
	}

	public Vector3d getVector3d() {
		return vector3d;
	}

	public net.minecraft.util.EnumFacing getFacing() {
		return direction;
	}

	public static EnumFacing fromMinecraft(net.minecraft.util.EnumFacing direction) {
		for (EnumFacing facing : values()) {
			if (facing.getFacing() == direction) {
				return facing;
			}
		}
		return NORTH;
	}

}
