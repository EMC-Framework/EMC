package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {

    @Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
    public void render(ExtendedBlockView world, BakedModel model, BlockState state, BlockPos pos, BufferBuilder vertexConsumer, boolean cull, Random random, long seed, CallbackInfoReturnable<Boolean> ci) {
        World.determineRenderState(state, pos, ci);
    }

}
