package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public class MixinBlockLiquid {

	/*@Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
	public void getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> ci) {
		EventVoxelShape event = new EventVoxelShape(Block.NULL_AABB, me.deftware.client.framework.world.block.Block.newInstance((Block) (Object) this));
		event.broadcast();
		if (event.modified) {
			ci.setReturnValue(event.shape);
		} else  if (GameMap.INSTANCE.get(GameKeys.FULL_LIQUID_VOXEL, false)) {
			ci.setReturnValue(Block.FULL_BLOCK_AABB);
		}
	}*/

}
