package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidRenderer.class)
public class MixinBlockFluidRenderer {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo cir) {
        if (!GameMap.INSTANCE.get(GameKeys.RENDER_FLUIDS, true))
            cir.cancel();
    }

}
