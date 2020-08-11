package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventBlockhardness;
import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.wrappers.world.IBlock;
import net.minecraft.block.*;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
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
    protected int lightLevel;

    @Shadow
    @Final
    protected boolean collidable;

    @Shadow public abstract Item asItem();

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1, CallbackInfoReturnable<VoxelShape> ci) {
        EventCollideCheck event = new EventCollideCheck(new IBlock(blockState_1.getBlock()));
        event.broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(VoxelShapes.empty());
            }
        } else {
            if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Registry.BLOCK.getRawId(blockState_1.getBlock()), "outline"))) {
                boolean doOutline = (boolean) SettingsMap.getValue(Registry.BLOCK.getRawId(blockState_1.getBlock()), "outline", true);
                if (!doOutline) {
                    ci.setReturnValue(VoxelShapes.empty());
                }
            }
        }
    }

    @Inject(method = "getSlipperiness", at = @At("TAIL"), cancellable = true)
    public void getSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (slipperiness != 0.6f) {
            Block block = Block.getBlockFromItem(this.asItem());
            EventSlowdown event = null;
            if (block instanceof IceBlock) {
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

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, Direction direction_1, CallbackInfoReturnable<Boolean> callback) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Registry.BLOCK.getRawId(blockState_1.getBlock()), "render"))) {
            callback.setReturnValue(
                    (boolean) SettingsMap.getValue(Registry.BLOCK.getRawId(blockState_1.getBlock()), "render", false));
        }
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void renderTypeSet(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Registry.BLOCK.getRawId(state.getBlock()), "render"))) {
            boolean doRender = (boolean) SettingsMap.getValue(Registry.BLOCK.getRawId(state.getBlock()), "render", false);
            if (!doRender) {
                cir.setReturnValue(BlockRenderType.INVISIBLE);
            }
        }
    }

    @Inject(method = "isTranslucent", at = @At("HEAD"), cancellable = true)
    public void getIsTranslucent(BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Registry.BLOCK.getRawId(state.getBlock()), "translucent"))) {
            cir.setReturnValue(
                    (boolean) SettingsMap.getValue(Registry.BLOCK.getRawId(state.getBlock()), "translucent", false));
        }
    }

    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    public void getLuminance(BlockState blockState_1, CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(
                (int) SettingsMap.getValue(Registry.BLOCK.getRawId(blockState_1.getBlock()), "lightValue", lightLevel));
    }

    @Inject(method = "calcBlockBreakingDelta", at = @At("HEAD"), cancellable = true)
    public void calcBlockBreakingDelta(BlockState blockState_1, PlayerEntity playerEntity_1, BlockView blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<Float> ci) {
        float float_1 = blockState_1.getHardness(blockView_1, blockPos_1);
        EventBlockhardness event = new EventBlockhardness();
        event.broadcast();
        if (float_1 < 0.0F) {
            ci.setReturnValue(0.0F);
        } else {
            ci.setReturnValue(!playerEntity_1.isUsingEffectiveTool(blockState_1) ? playerEntity_1.getBlockBreakingSpeed(blockState_1) / float_1 / 100.0F
                    : playerEntity_1.getBlockBreakingSpeed(blockState_1) / float_1 / 30.0F * event.getMultiplier());
        }
    }

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    public void getCollisionShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext context, CallbackInfoReturnable<VoxelShape> ci) {
        EventVoxelShape event = new EventVoxelShape(collidable ? blockState_1.getOutlineShape(blockView_1, blockPos_1) : VoxelShapes.empty(), new IBlock((Block) (Object) this));
        event.broadcast();
        if (event.modified) {
            ci.setReturnValue(event.shape);
        } else {
            if (Block.getBlockFromItem(this.asItem()) instanceof FluidBlock) {
                ci.setReturnValue((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "LIQUID_VOXEL_FULL", false)
                        ? VoxelShapes.fullCube()
                        : VoxelShapes.empty());
            } else if (Block.getBlockFromItem(this.asItem()) instanceof SweetBerryBushBlock) {
                if ((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "custom_berry_voxel", false)) {
                    ci.setReturnValue(VoxelShapes.fullCube());
                }
            }
        }
    }

}
