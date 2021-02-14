package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.mixin.imp.IMixinAbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
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
    private BlockState blockState;

    @Shadow
    @Final
    protected int lightValue;

    // FIXME
    /*
    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(IBlockState blockState_1, IBlockAccess blockView_1, BlockPos blockPos_1, CallbackInfoReturnable<AxisAlignedBB> ci) {
        EventCollideCheck event = new EventCollideCheck(me.deftware.client.framework.world.block.Block.newInstance(blockState_1.getBlock()));
        event.broadcast();
        if (event.updated) {
            if (event.canCollide) {
                ci.setReturnValue(Block.NULL_AABB);
            }
        } else {
            if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.blockRegistry.getIDForObject(blockState_1.getBlock()), "outline"))) {
                boolean doOutline = (boolean) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockState_1.getBlock()), "outline", true);
                if (!doOutline) {
                    ci.setReturnValue(Block.NULL_AABB);
                }
            }
        }
    }*/

    @Inject(method = "getLightValue", at = @At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(
                (int) SettingsMap.getValue(Block.blockRegistry.getIDForObject(blockState.getBlock()), "lightValue", lightValue));
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

    @Override
    public float getTheSlipperiness() {
        return this.slipperiness;
    }

}
