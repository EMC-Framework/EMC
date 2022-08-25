package me.deftware.mixin.mixins.integration;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.ModelData;
import net.optifine.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *  Compatible with OptiFine H9
 *
 * @author Deftware
 */
@Pseudo
@SuppressWarnings({"ShadowTarget", "UnresolvedMixinReference"})
@Mixin(BlockModelRenderer.class)
public abstract class MixinOptiFineBlockModelRenderer {

    @Shadow(remap = false)
    public abstract void renderModelSmooth(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack buffer, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, ModelData data, RenderLayer layer);

    @Shadow(remap = false)
    public abstract void renderModelFlat(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack buffer, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, ModelData data, RenderLayer layer);

    @Inject(method = "renderModelSmooth", at = @At("HEAD"), remap = false, cancellable = true)
    public void renderModelSmoothInject(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, ModelData data, RenderLayer layer, CallbackInfo ci) {
        onBlockModelRender(state.getBlock(), cull, ci, () -> renderModelSmooth(world, model, state, pos, matrix, vertexConsumer, false, random, seed, overlay, data, layer));
    }

    @Inject(method = "renderModelFlat", at = @At("HEAD"), remap = false, cancellable = true)
    public void renderModelFlatInject(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack buffer, VertexConsumer vertexConsumer, boolean cull, Random random, long l, int i, ModelData data, RenderLayer layer, CallbackInfo ci) {
        onBlockModelRender(state.getBlock(), cull, ci, () -> renderModelFlat(world, model, state, pos, buffer, vertexConsumer, false, random, l, i, data, layer));
    }

    @Unique
    private void onBlockModelRender(Block block, boolean cull, CallbackInfo ci, Runnable runnable) {
        FrameworkConstants.CAN_RENDER_SHADER = !Config.isShaders();
        try {
            BlockPropertyManager blockProperties = Bootstrap.blockProperties;
            if (blockProperties.isActive() && !blockProperties.isOpacityMode()) {
                int id = Registry.BLOCK.getRawId(block);
                if (!(blockProperties.contains(id) && blockProperties.get(id).isRender())) {
                    ci.cancel();
                } else if (cull) {
                    runnable.run();
                    ci.cancel();
                }
            }
        } catch (Exception ignored) { }
    }

}
