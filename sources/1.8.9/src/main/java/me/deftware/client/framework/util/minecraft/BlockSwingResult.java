package me.deftware.client.framework.util.minecraft;

import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.Vector3;
import net.minecraft.util.Vec3;

/**
 * @author Deftware
 */
public class BlockSwingResult extends MovingObjectPosition {

	public BlockSwingResult(Vector3<Double> vector3d, EnumFacing facing, BlockPosition position, boolean inBlock) {
		super(
				MovingObjectType.BLOCK,
				(Vec3) vector3d,
				facing.getFacing(),
				(BlockPos) position
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
		return ClientWorld.getClientWorld()._getBlockFromPosition(getBlockPosition());
	}

	public BlockPosition getBlockPosition() {
		return (BlockPosition) getBlockPos();
	}

	public EnumFacing getFacing() {
		return EnumFacing.fromMinecraft(sideHit);
	}

}
