package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {

    protected boolean blocksMovement;

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void shouldDrawSide(IBlockState blockState_1, IBlockAccess blockView_1, BlockPos blockPos_1, EnumFacing direction_1, CallbackInfoReturnable<Boolean> callback) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.REGISTRY.getIDForObject(blockState_1.getBlock()), "render"))) {
            callback.setReturnValue(
                    (boolean) SettingsMap.getValue(Block.REGISTRY.getIDForObject(blockState_1.getBlock()), "render", false));
        }
    }

    @Inject(method = "isOpaqueCube", at = @At("HEAD"), cancellable = true)
    public void getIsTranslucent(IBlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.REGISTRY.getIDForObject(state.getBlock()), "translucent"))) {
            cir.setReturnValue(
                    (boolean) SettingsMap.getValue(Block.REGISTRY.getIDForObject(state.getBlock()), "translucent", false));
        }
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(IBlockState p_getShapeForCollision_1_, IBlockAccess p_getShapeForCollision_2_, BlockPos p_getShapeForCollision_3_, CallbackInfoReturnable<AxisAlignedBB> ci) {
        EventVoxelShape event = new EventVoxelShape(blocksMovement ? p_getShapeForCollision_1_.getCollisionBoundingBox(p_getShapeForCollision_2_, p_getShapeForCollision_3_) : Block.NULL_AABB, me.deftware.client.framework.world.block.Block.newInstance((Block) (Object) this));
        event.broadcast();
        if (event.modified) {
            ci.setReturnValue(event.shape);
        }
    }

}
