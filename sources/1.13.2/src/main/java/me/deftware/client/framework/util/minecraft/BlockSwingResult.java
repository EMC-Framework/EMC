package me.deftware.client.framework.util.minecraft;

import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.math.RayTraceResult;
import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.Vector3;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author Deftware
 */
public class BlockSwingResult extends RayTraceResult {

	public BlockSwingResult(Vector3<Double> vector3d, EnumFacing facing, BlockPosition position, boolean inBlock) {
		super(
				Type.BLOCK,
				(Vec3d) vector3d,
				facing.getFacing(),
				(BlockPos) position
		);
	}

	public BlockSwingResult(RayTraceResult result) {
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
