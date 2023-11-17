package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {

    @Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
    public void render(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, BufferBuilder vertexConsumer, boolean cull, Random random, long seed, CallbackInfoReturnable<Boolean> ci) {
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (blockProperties.isActive() && !blockProperties.isOpacityMode()) {
            int id = Registry.BLOCK.getRawId(state.getBlock());
            if (!(blockProperties.contains(id) && blockProperties.get(id).isRender()))
                ci.setReturnValue(false);
        }
    }

}
