package me.deftware.mixin.mixins;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 *  OptiFine overwrites hooks used by EMC for block render override, so we need to inject into OptiFine and re-add those hooks
 * @author Deftware
 */
@SuppressWarnings("ALL")
@Mixin(BlockModelRenderer.class)
public abstract class MixinOptiFineBlockModelRenderer {

    @Shadow(remap = false)
    public abstract boolean renderModelSmooth(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack buffer, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, IModelData data);

    @Shadow(remap = false)
    public abstract boolean renderModelFlat(BlockRenderView view, BakedModel model, BlockState state, BlockPos pos, MatrixStack buffer, VertexConsumer vertexConsumer, boolean cull, Random random, long l, int i, IModelData data);

    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    public void renderModel(BlockRenderView blockRenderView_1, BakedModel bakedModel_1, BlockState blockState_1, BlockPos blockPos_1, MatrixStack matrixStack_1, VertexConsumer vertexConsumer_1, boolean boolean_1, Random random_1, long long_1, int int_1, IModelData data, CallbackInfoReturnable<Boolean> ci) {
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

    @Inject(method = "renderModelSmooth", at = @At("RETURN"), remap = false, cancellable = true)
    public void renderModelSmoothInject(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack buffer, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, IModelData data, CallbackInfoReturnable<Boolean> ci) {
        if (cull) {
            ci.setReturnValue(renderModelSmooth(world, model, state, pos, buffer, vertexConsumer, false, random, seed, overlay, data));
        }
    }

    @Inject(method = "renderModelFlat", at = @At("HEAD"), remap = false, cancellable = true)
    public void renderModelFlatInject(BlockRenderView view, BakedModel model, BlockState state, BlockPos pos, MatrixStack buffer, VertexConsumer vertexConsumer, boolean cull, Random random, long l, int i, IModelData data, CallbackInfoReturnable<Boolean> ci) {
        if (cull) {
            ci.setReturnValue(renderModelFlat(view, model, state, pos, buffer, vertexConsumer, false, random, l, i, data));
        }
    }

}
