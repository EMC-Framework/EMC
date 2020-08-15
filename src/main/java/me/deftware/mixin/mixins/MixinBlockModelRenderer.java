package me.deftware.mixin.mixins;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {

    @Inject(method = "renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;Z)Z", at = @At("HEAD"), cancellable = true)
    private void renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder buffer, boolean checkSides, CallbackInfoReturnable<Boolean> ci) {
        if (blockStateIn.getBlock() instanceof BlockLiquid) {
            ci.setReturnValue(((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "FLUIDS", true)));
        } else {
            if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Block.REGISTRY.getIDForObject(blockStateIn.getBlock()), "render"))) {
                boolean doRender = (boolean) SettingsMap.getValue(Block.REGISTRY.getIDForObject(blockStateIn.getBlock()), "render", false);
                if (!doRender) {
                    ci.setReturnValue(false);
                }
            }
        }
    }

    /*@ModifyArgs(method = "render(Lnet/minecraft/block/BlockState;Lnet/minecraft/client/render/model/BakedModel;FFFF)V", at = @At("HEAD"))
    public float render(float float_1, float float_2, float float_3, float float_4, CallbackInfo ci) {
        if (blockState_1 != null) {
            try {
                float_1 = (float) SettingsMap.getValue(Registry.BLOCK.getRawId(blockState_1.getBlock()),
                        "lightValue", float_1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return float_1;
    }*/

}
