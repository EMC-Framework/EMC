package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.math.box.VoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Shadow
    @Final
    private BlockState blockState;

    protected boolean blocksMovement;

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void shouldDrawSide(IBlockAccess blockView_1, BlockPos blockPos_1, EnumFacing direction_1, CallbackInfoReturnable<Boolean> callback) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "render"))) {
            callback.setReturnValue(
                    (boolean) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "render", false));
        }
    }

    @Inject(method = "isOpaqueCube", at = @At("HEAD"), cancellable = true)
    public void getIsTranslucent(CallbackInfoReturnable<Boolean> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "translucent"))) {
            cir.setReturnValue(
                    (boolean) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "translucent", false));
        }
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(World p_getShapeForCollision_1_, BlockPos p_getShapeForCollision_2_, IBlockState p_getShapeForCollision_3_, CallbackInfoReturnable<AxisAlignedBB> ci) {
        me.deftware.client.framework.world.block.Block block = me.deftware.client.framework.world.block.Block.newInstance((Block) (Object) this);
        EventVoxelShape event = new EventVoxelShape(blocksMovement ? block.getMinecraftBlock().getCollisionBoundingBox(p_getShapeForCollision_1_, p_getShapeForCollision_2_, p_getShapeForCollision_3_) : VoxelShape.EMPTY.getMinecraftVoxelShape(), block);
        event.broadcast();
        if (event.modified) {
            ci.setReturnValue(event.shape);
        } else {
            if (blockState.getBlock() instanceof BlockLiquid) {
                ci.setReturnValue((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "LIQUID_VOXEL_FULL", false)
                        ? VoxelShape.SOLID.getMinecraftVoxelShape()
                        : VoxelShape.EMPTY.getMinecraftVoxelShape());
            }
        }
    }


}
