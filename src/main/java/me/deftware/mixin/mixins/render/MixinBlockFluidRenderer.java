package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public class MixinBlockFluidRenderer {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, FluidState state, CallbackInfoReturnable<Boolean> ci) {
        if (!((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "FLUIDS", true))) {
            ci.cancel();
        }
    }

}