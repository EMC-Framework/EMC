package me.deftware.client.framework.world;

import me.deftware.client.framework.math.Vector3;

/**
 * @author Deftware
 */
public enum EnumFacing {

	DOWN(net.minecraft.util.EnumFacing.DOWN), UP(net.minecraft.util.EnumFacing.UP), NORTH(net.minecraft.util.EnumFacing.NORTH), SOUTH(net.minecraft.util.EnumFacing.SOUTH), WEST(net.minecraft.util.EnumFacing.WEST), EAST(net.minecraft.util.EnumFacing.EAST);

	private final net.minecraft.util.EnumFacing direction;

	EnumFacing(net.minecraft.util.EnumFacing direction) {
		this.direction = direction;
	}

	public Vector3<Integer> getVector() {
		return (Vector3<Integer>) direction.getDirectionVec();
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
