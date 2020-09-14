package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventBlockhardness;
import me.deftware.client.framework.event.events.EventCollideCheck;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.mixin.imp.IMixinAbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinAbstractBlock implements IMixinAbstractBlock {

    @Shadow @Final protected float slipperiness;

    @Shadow
    @Final
    protected int lightValue;

    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockState blockState_1, IBlockAccess blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<AxisAlignedBB> ci) {
        EventCollideCheck event = new EventCollideCheck(me.deftware.client.framework.world.block.Block.newInstance(blockState_1.getBlock()));
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

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(IBlockState blockState_1, CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(
                (int) SettingsMap.getValue(Block.REGISTRY.getIDForObject(blockState_1.getBlock()), "lightValue", lightValue));
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

    @Override
    public float getTheSlipperiness() {
        return this.slipperiness;
    }

}
