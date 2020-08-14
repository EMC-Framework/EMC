package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventBlockhardness;
import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.wrappers.world.IBlock;
import net.minecraft.block.*;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Final
    @Shadow
    private float slipperiness;

    @Shadow
    @Final
    protected int lightValue;

    @Shadow
    @Final
    protected boolean blocksMovement;

    @Shadow public abstract Item asItem();

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockState blockState_1, IBlockReader blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<VoxelShape> ci) {
        EventCollideCheck event = new EventCollideCheck(new IBlock(blockState_1.getBlock()));
        event.broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(VoxelShapes.empty());
            }
        } else {
            if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(IRegistry.BLOCK.getId(blockState_1.getBlock()), "outline"))) {
                boolean doOutline = (boolean) SettingsMap.getValue(IRegistry.BLOCK.getId(blockState_1.getBlock()), "outline", true);
                if (!doOutline) {
                    ci.setReturnValue(VoxelShapes.empty());
                }
            }
        }
    }

    @Inject(method = "getSlipperiness()F", at = @At("TAIL"), cancellable = true)
    public void getSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (slipperiness != 0.6f) {
            Block block = Block.getBlockFromItem(this.asItem());
            EventSlowdown event = null;
            if (block instanceof BlockIce) {
                event = new EventSlowdown(EventSlowdown.SlowdownType.Slipperiness, 0.6f);
            }
            if (event != null) {
                event.broadcast();
                if (event.isCanceled()) {
                    cir.setReturnValue(event.getMultiplier());
                }
            }
        }
    }

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(IBlockState blockState_1, IBlockReader blockView_1, BlockPos blockPos_1, EnumFacing direction_1, CallbackInfoReturnable<Boolean> callback) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(IRegistry.BLOCK.getId(blockState_1.getBlock()), "render"))) {
            callback.setReturnValue(
                    (boolean) SettingsMap.getValue(IRegistry.BLOCK.getId(blockState_1.getBlock()), "render", false));
        }
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void renderTypeSet(IBlockState state, CallbackInfoReturnable<EnumBlockRenderType> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(IRegistry.BLOCK.getId(state.getBlock()), "render"))) {
            boolean doRender = (boolean) SettingsMap.getValue(IRegistry.BLOCK.getId(state.getBlock()), "render", false);
            if (!doRender) {
                cir.setReturnValue(EnumBlockRenderType.INVISIBLE);
            }
        }
    }

    @Inject(method = "isOpaqueCube", at = @At("HEAD"), cancellable = true)
    public void getIsTranslucent(IBlockState state, IBlockReader view, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(IRegistry.BLOCK.getId(state.getBlock()), "translucent"))) {
            cir.setReturnValue(
                    (boolean) SettingsMap.getValue(IRegistry.BLOCK.getId(state.getBlock()), "translucent", false));
        }
    }

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(IBlockState blockState_1, CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(
                (int) SettingsMap.getValue(IRegistry.BLOCK.getId(blockState_1.getBlock()), "lightValue", lightValue));
    }

    @Inject(method = "getPlayerRelativeBlockHardness", at = @At("HEAD"), cancellable = true)
    public void calcBlockBreakingDelta(IBlockState state, EntityPlayer player, IBlockReader reader, BlockPos pos, CallbackInfoReturnable<Float> ci) {
        float f = state.getBlockHardness(reader, pos);
        EventBlockhardness event = new EventBlockhardness();
        event.broadcast();
        if (f < 0.0F) {
            ci.setReturnValue(0.0F);
        } else {
            ci.setReturnValue(!player.canHarvestBlock(state) ? player.getDigSpeed(state) / f / 100.0F
                    : player.getDigSpeed(state) / f / 30.0F * event.getMultiplier());
        }
    }


    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    public void getShapeForCollision(IBlockState p_getShapeForCollision_1_, IBlockReader p_getShapeForCollision_2_, BlockPos p_getShapeForCollision_3_, CallbackInfoReturnable<VoxelShape> ci) {
        EventVoxelShape event = new EventVoxelShape(blocksMovement ? p_getShapeForCollision_1_.getShape(p_getShapeForCollision_2_, p_getShapeForCollision_3_) : VoxelShapes.empty(), new IBlock((Block) (Object) this));
        event.broadcast();
        if (event.modified) {
            ci.setReturnValue(event.shape);
        } else {
            if (Block.getBlockFromItem(this.asItem()) instanceof BlockFlowingFluid) {
                ci.setReturnValue((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "LIQUID_VOXEL_FULL", false)
                        ? VoxelShapes.fullCube()
                        : VoxelShapes.empty());
            }
        }
    }


}
