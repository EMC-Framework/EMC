package me.deftware.mixin.mixins;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ExtendedBlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {

    @Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
    public void tesselate(ExtendedBlockView extendedBlockView_1, BakedModel bakedModel_1, BlockState blockState_1, BlockPos blockPos_1, BufferBuilder bufferBuilder_1, boolean boolean_1, Random random_1, long long_1, CallbackInfoReturnable<Boolean> ci) {
        if (blockState_1.getBlock() instanceof FluidBlock) {
            ci.setReturnValue(((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "FLUIDS", true)));
        } else {
            if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(Registry.BLOCK.getRawId(blockState_1.getBlock()), "render"))) {
                boolean doRender = (boolean) SettingsMap.getValue(Registry.BLOCK.getRawId(blockState_1.getBlock()), "render", false);
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
