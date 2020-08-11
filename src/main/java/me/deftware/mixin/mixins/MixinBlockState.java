package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventBlockhardness;
import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.wrappers.world.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ShapeUtils;
import net.minecraft.util.math.shapes.VoxelShape;
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

    @Shadow public abstract VoxelShape getShape(IBlockReader p_getShape_1_, BlockPos p_getShape_2_);

    @Shadow public abstract float getBlockHardness(IBlockReader p_getBlockHardness_1_, BlockPos p_getBlockHardness_2_);

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockReader blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<VoxelShape> ci) {
        EventCollideCheck event = new EventCollideCheck(new IBlock(this.getBlock()));
        event.broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(ShapeUtils.empty());
            }
        } else {
            if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.REGISTRY.getId(this.getBlock()), "outline"))) {
                boolean doOutline = (boolean) SettingsMap.getValue(Block.REGISTRY.getId(this.getBlock()), "outline", true);
                if (!doOutline) {
                    ci.setReturnValue(ShapeUtils.empty());
                }
            }
        }
    }

    @Inject(method = "isOpaqueCube", at = @At("HEAD"), cancellable = true)
    public void getIsTranslucent(IBlockReader view, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.REGISTRY.getId(this.getBlock()), "translucent"))) {
            cir.setReturnValue(
                    (boolean) SettingsMap.getValue(Block.REGISTRY.getId(this.getBlock()), "translucent", false));
        }
    }

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(
                (int) SettingsMap.getValue(Block.REGISTRY.getId(this.getBlock()), "lightValue", this.getBlock().getLightValue(this.getBlock().getDefaultState())));
    }

    @Inject(method = "getPlayerRelativeBlockHardness", at = @At("HEAD"), cancellable = true)
    public void calcBlockBreakingDelta(EntityPlayer player, IBlockReader blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<Float> ci) {
        float f = this.getBlockHardness(blockView_1, blockPos_1);
        EventBlockhardness event = new EventBlockhardness();
        event.broadcast();
        if (f < 0.0F) {
            ci.setReturnValue(0.0F);
        } else {
            ci.setReturnValue(!player.canHarvestBlock(this.getBlock().getDefaultState()) ? player.getDigSpeed(this.getBlock().getDefaultState()) / f / 100.0F
                    : player.getDigSpeed(this.getBlock().getDefaultState()) / f / 30.0F * event.getMultiplier());
        }
    }

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    public void getCollisionShape(IBlockReader blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<VoxelShape> ci) {
        EventVoxelShape event = new EventVoxelShape(this.getShape(blockView_1, blockPos_1), new IBlock(this.getBlock()));
        event.broadcast();
        if (event.modified) {
            ci.setReturnValue(event.shape);
        } else {
            if (this.getBlock() instanceof BlockFlowingFluid) {
                ci.setReturnValue((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "LIQUID_VOXEL_FULL", false)
                        ? ShapeUtils.fullCube()
                        : ShapeUtils.empty());
            }
        }
    }
}
