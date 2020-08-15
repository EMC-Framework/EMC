package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventBlockhardness;
import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.wrappers.math.IAxisAlignedBB;
import me.deftware.client.framework.wrappers.world.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
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

    @Shadow
    protected int lightValue;

    protected boolean blocksMovement;

    // FIXME
    /*@Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockState blockState_1, IBlockAccess blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<AxisAlignedBB> ci) {
        EventCollideCheck event = new EventCollideCheck(new IBlock(blockState_1.getBlock()));
        event.broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(IAxisAlignedBB.NULL_AABB);
            }
        } else {
            if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.blockRegistry.getIDForObject(blockState_1.getBlock()), "outline"))) {
                boolean doOutline = (boolean) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockState_1.getBlock()), "outline", true);
                if (!doOutline) {
                    ci.setReturnValue(IAxisAlignedBB.NULL_AABB);
                }
            }
        }
    }*/

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void shouldDrawSide(IBlockAccess blockView_1, BlockPos blockPos_1, EnumFacing direction_1, CallbackInfoReturnable<Boolean> callback) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "render"))) {
            callback.setReturnValue(
                    (boolean) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "render", false));
        }
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void renderTypeSet(CallbackInfoReturnable<Integer> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "render"))) {
            boolean doRender = (boolean) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "render", false);
            if (!doRender) {
                cir.setReturnValue(0);
            }
        }
    }

    @Inject(method = "isOpaqueCube", at = @At("HEAD"), cancellable = true)
    public void getIsTranslucent(CallbackInfoReturnable<Boolean> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "translucent"))) {
            cir.setReturnValue(
                    (boolean) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "translucent", false));
        }
    }

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(
                (int) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "lightValue", lightValue));
    }

    @Inject(method = "getPlayerRelativeBlockHardness", at = @At("HEAD"), cancellable = true)
    public void calcBlockBreakingDelta(EntityPlayer player, World reader, BlockPos pos, CallbackInfoReturnable<Float> ci) {
        float f = blockState.getBlock().getBlockHardness(reader, pos);
        EventBlockhardness event = new EventBlockhardness();
        event.broadcast();
        if (f < 0.0F) {
            ci.setReturnValue(0.0F);
        } else {
            ci.setReturnValue(!player.canHarvestBlock(blockState.getBlock()) ? player.getToolDigEfficiency(blockState.getBlock()) / f / 100.0F
                    : player.getToolDigEfficiency(blockState.getBlock()) / f / 30.0F * event.getMultiplier());
        }
    }

    @Inject(method = "getBlockLayer", at = @At("HEAD"), cancellable = true)
    private void getRenderLayer(CallbackInfoReturnable<EnumWorldBlockLayer> ci) {
        if (SettingsMap.isOverrideMode()) {
            if ((boolean) SettingsMap.getValue(Block.getIdFromBlock(blockState.getBlock()), "translucent", true)) {
                ci.setReturnValue(EnumWorldBlockLayer.TRANSLUCENT);
            }
        }
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(World p_getShapeForCollision_1_, BlockPos p_getShapeForCollision_2_, IBlockState p_getShapeForCollision_3_, CallbackInfoReturnable<AxisAlignedBB> ci) {
        IBlock block = new IBlock((Block) (Object) this);
        EventVoxelShape event = new EventVoxelShape(blocksMovement ? block.getBlock().getCollisionBoundingBox(p_getShapeForCollision_1_, p_getShapeForCollision_2_, p_getShapeForCollision_3_) : IAxisAlignedBB.NULL_AABB, block);
        event.broadcast();
        if (event.modified) {
            ci.setReturnValue(event.shape);
        } else {
            if (blockState.getBlock() instanceof BlockLiquid) {
                ci.setReturnValue((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "LIQUID_VOXEL_FULL", false)
                    ? IAxisAlignedBB.FULL_BLOCK_AABB
                    : IAxisAlignedBB.NULL_AABB);
            }
        }
    }

}
