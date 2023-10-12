package me.deftware.mixin.mixins.integration;

import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("InvalidInjectorMethodSignature")
@Mixin(value = FluidRenderer.class, remap = false)
public class MixinSodiumFluidRenderer {

    @Inject(method = "render", at = @At("HEAD"), remap = false, cancellable = true)
    private void onRender(WorldSlice world, FluidState fluidState, BlockPos blockPos, BlockPos offset, ChunkBuildBuffers buffers, CallbackInfo ci) {
        if (!GameMap.INSTANCE.get(GameKeys.RENDER_FLUIDS, true))
            ci.cancel();
    }

}
