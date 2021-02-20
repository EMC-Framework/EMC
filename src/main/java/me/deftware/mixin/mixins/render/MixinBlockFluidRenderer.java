package me.deftware.mixin.mixins.render;

import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.fluid.IFluidState;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockFluidRenderer.class)
public class MixinBlockFluidRenderer {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(IWorldReader worldReader, BlockPos blockPos, BufferBuilder bufferBuilder, IFluidState fluidState, CallbackInfoReturnable<Boolean> ci) {
        if (!GameMap.INSTANCE.get(GameKeys.RENDER_FLUIDS, true))
            ci.cancel();
    }
}
