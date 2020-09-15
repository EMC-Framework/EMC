package me.deftware.client.framework.util.minecraft;

import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.DoubleBlockPosition;
import me.deftware.client.framework.math.vector.Vector3d;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.client.framework.world.World;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.MovingObjectPosition;

/**
 * @author Deftware
 */
public class BlockSwingResult extends MovingObjectPosition {

	public BlockSwingResult(Vector3d vector3d, EnumFacing facing, BlockPosition position, boolean inBlock) {
		super(
				MovingObjectType.BLOCK,
				vector3d.getMinecraftVector(),
				facing.getFacing(),
				position.getMinecraftBlockPos()
		);
	}

	public BlockSwingResult(MovingObjectPosition result) {
		super(
				result.hitVec,
				result.sideHit,
				result.getBlockPos()
		);
	}

	public Block getBlock() {
		return World.getBlockFromPosition(getBlockPosition());
	}

	public BlockPosition getBlockPosition() {
		return DoubleBlockPosition.fromMinecraftBlockPos(getBlockPos());
	}

	public EnumFacing getFacing() {
		return EnumFacing.fromMinecraft(sideHit);
	}

}
