package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventBlockhardness;
import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IBlockState.class)
public abstract class MixinBlockState {

    @Shadow
    public abstract Block getBlock();

    @Shadow public abstract float getBlockHardness(IBlockReader p_getBlockHardness_1_, BlockPos p_getBlockHardness_2_);

    @Shadow public abstract VoxelShape getShape(IBlockReader p_getShape_1_, BlockPos p_getShape_2_);

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockReader blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<VoxelShape> ci) {
        EventCollideCheck event = new EventCollideCheck(me.deftware.client.framework.world.block.Block.newInstance(this.getBlock()));
        event.broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(VoxelShapes.empty());
            }
        } else {
            if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(IRegistry.BLOCK.getId(this.getBlock()), "outline"))) {
                boolean doOutline = (boolean) SettingsMap.getValue(IRegistry.BLOCK.getId(this.getBlock()), "outline", true);
                if (!doOutline) {
                    ci.setReturnValue(VoxelShapes.empty());
                }
            }
        }
    }

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    public void getCollisionShape(IBlockReader blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<VoxelShape> ci) {
        EventVoxelShape event = new EventVoxelShape(this.getShape(blockView_1, blockPos_1), me.deftware.client.framework.world.block.Block.newInstance(this.getBlock()));
        event.broadcast();
        if (event.modified) {
            ci.setReturnValue(event.shape);
        } else {
            if (this.getBlock() instanceof BlockFlowingFluid) {
                ci.setReturnValue((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "LIQUID_VOXEL_FULL", false)
                        ? VoxelShapes.fullCube()
                        : VoxelShapes.empty());
            }
        }
    }

}
