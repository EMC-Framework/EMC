package me.deftware.client.framework.util.minecraft;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.util.hitresult.CrosshairResult;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author Deftware
 */
public class BlockSwingResult extends CrosshairResult {

	public BlockSwingResult(Vector3<Double> vector3d, EnumFacing facing, BlockPosition position, boolean inBlock) {
		super(new BlockHitResult(
				(Vec3d) vector3d,
				facing.getFacing(),
				(BlockPos) position,
				inBlock
		));
	}

	public BlockSwingResult(HitResult result) {
		super(result);
	}

	public Block getBlock() {
		return ClientWorld.getClientWorld()._getBlockFromPosition(getBlockPosition());
	}

	public BlockPosition getBlockPosition() {
		return (BlockPosition) getMinecraftHitResult().getBlockPos();
	}

	public EnumFacing getFacing() {
		return EnumFacing.fromMinecraft(getMinecraftHitResult().getSide());
	}

	@Override
	public BlockHitResult getMinecraftHitResult() {
		return (BlockHitResult) hitResult;
	}

}
