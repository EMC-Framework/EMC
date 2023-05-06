package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public class MixinBlockLiquid {

	@Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
	public void getCollisionBoundingBox(IBlockState p_getShapeForCollision_1_, IBlockAccess p_getShapeForCollision_2_, BlockPos p_getShapeForCollision_3_, CallbackInfoReturnable<AxisAlignedBB> ci) {
		EventVoxelShape event = new EventVoxelShape(Block.NULL_AABB, (me.deftware.client.framework.world.block.Block) this);
		event.broadcast();
		if (event.modified) {
			ci.setReturnValue(event.shape);
		} else  if (GameMap.INSTANCE.get(GameKeys.FULL_LIQUID_VOXEL, false)) {
			ci.setReturnValue(Block.FULL_BLOCK_AABB);
		}
	}

}
