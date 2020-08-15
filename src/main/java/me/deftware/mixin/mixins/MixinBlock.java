package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventBlockhardness;
import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.event.events.EventVoxelShape;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.wrappers.world.IBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
    private BlockStateContainer blockState;

    @Shadow
    protected int lightValue;

    protected boolean blocksMovement;

    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockState blockState_1, IBlockAccess blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<AxisAlignedBB> ci) {
        EventCollideCheck event = new EventCollideCheck(new IBlock(blockState_1.getBlock()));
        event.broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(Block.NULL_AABB);
            }
        } else {
            if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.REGISTRY.getIDForObject(blockState_1.getBlock()), "outline"))) {
                boolean doOutline = (boolean) SettingsMap.getValue(Block.REGISTRY.getIDForObject(blockState_1.getBlock()), "outline", true);
                if (!doOutline) {
                    ci.setReturnValue(Block.NULL_AABB);
                }
            }
        }
    }

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void shouldDrawSide(IBlockState blockState_1, IBlockAccess blockView_1, BlockPos blockPos_1, EnumFacing direction_1, CallbackInfoReturnable<Boolean> callback) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.REGISTRY.getIDForObject(blockState_1.getBlock()), "render"))) {
            callback.setReturnValue(
                    (boolean) SettingsMap.getValue(Block.REGISTRY.getIDForObject(blockState_1.getBlock()), "render", false));
        }
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void renderTypeSet(IBlockState state, CallbackInfoReturnable<EnumBlockRenderType> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.REGISTRY.getIDForObject(state.getBlock()), "render"))) {
            boolean doRender = (boolean) SettingsMap.getValue(Block.REGISTRY.getIDForObject(state.getBlock()), "render", false);
            if (!doRender) {
                cir.setReturnValue(EnumBlockRenderType.INVISIBLE);
            }
        }
    }

    @Inject(method = "isOpaqueCube", at = @At("HEAD"), cancellable = true)
    public void getIsTranslucent(IBlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.REGISTRY.getIDForObject(state.getBlock()), "translucent"))) {
            cir.setReturnValue(
                    (boolean) SettingsMap.getValue(Block.REGISTRY.getIDForObject(state.getBlock()), "translucent", false));
        }
    }

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(IBlockState blockState_1, CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(
                (int) SettingsMap.getValue(Block.REGISTRY.getIDForObject(blockState_1.getBlock()), "lightValue", lightValue));
    }

    @Inject(method = "getPlayerRelativeBlockHardness", at = @At("HEAD"), cancellable = true)
    public void calcBlockBreakingDelta(IBlockState state, EntityPlayer player, World reader, BlockPos pos, CallbackInfoReturnable<Float> ci) {
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

    @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
    private void getRenderLayer(CallbackInfoReturnable<BlockRenderLayer> ci) {
        if (SettingsMap.isOverrideMode()) {
            if ((boolean) SettingsMap.getValue(Block.getIdFromBlock(blockState.getBlock()), "translucent", true)) {
                ci.setReturnValue(BlockRenderLayer.TRANSLUCENT);
            }
        }
    }

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getCollisionBoundingBox(IBlockState p_getShapeForCollision_1_, IBlockAccess p_getShapeForCollision_2_, BlockPos p_getShapeForCollision_3_, CallbackInfoReturnable<AxisAlignedBB> ci) {
        EventVoxelShape event = new EventVoxelShape(blocksMovement ? p_getShapeForCollision_1_.getCollisionBoundingBox(p_getShapeForCollision_2_, p_getShapeForCollision_3_) : Block.NULL_AABB, new IBlock((Block) (Object) this));
        event.broadcast();
        if (event.modified) {
            ci.setReturnValue(event.shape);
        } else {
            if (p_getShapeForCollision_1_.getBlock() instanceof BlockLiquid) {
                ci.setReturnValue((boolean) SettingsMap.getValue(SettingsMap.MapKeys.BLOCKS, "LIQUID_VOXEL_FULL", false)
                        ? Block.FULL_BLOCK_AABB
                        : Block.NULL_AABB);
            }
        }
    }

}
