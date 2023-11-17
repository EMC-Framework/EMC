package me.deftware.client.framework.world;

import me.deftware.client.framework.math.Vector3;
import net.minecraft.util.math.Direction;

/**
 * @author Deftware
 */
public enum EnumFacing {

	DOWN(Direction.DOWN), UP(Direction.UP), NORTH(Direction.NORTH), SOUTH(Direction.SOUTH), WEST(Direction.WEST), EAST(Direction.EAST);

	private final Direction direction;

	EnumFacing(Direction direction) {
		this.direction = direction;
	}

	public Vector3<Integer> getVector() {
		return (Vector3<Integer>) direction.getVector();
	}

	public Direction getFacing() {
		return direction;
	}

	public static EnumFacing fromMinecraft(Direction direction) {
		for (EnumFacing facing : values()) {
			if (facing.getFacing() == direction) {
				return facing;
			}
		}
		return NORTH;
	}

}
