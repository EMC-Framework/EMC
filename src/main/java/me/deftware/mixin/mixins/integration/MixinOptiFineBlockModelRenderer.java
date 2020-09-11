package me.deftware.mixin.mixins.integration;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.optifine.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 *  OptiFine overwrites hooks used by EMC for block render override, so we need to inject into OptiFine and re-add those hooks
 *
 *  NOTE! This only works for Optifine F1 - OptiFine F5
 *  TODO: This Mixin is defunct in 1.16.2, and needs repairs to restore functionality
 *
 * @author Deftware
 */
@SuppressWarnings("ALL")
@Mixin(BlockModelRenderer.class)
public abstract class MixinOptiFineBlockModelRenderer {

    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    public void renderModel(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, BufferBuilder buffer, boolean cull, Random random, long seed, IModelData data, CallbackInfoReturnable<Boolean> ci) {
        World.determineRenderState(state, pos, ci);
    }

    @ModifyArg(method = {"renderModel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModelRenderer;tesselateFlat(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/BufferBuilder;ZLjava/util/Random;J)Z"))
    private boolean renderModelFlat1(boolean checkSides) {
        FrameworkConstants.CAN_RENDER_SHADER = !Config.isShaders();
        try {
            if (SettingsMap.isOverrideMode()) {
                return false;
            }
        } catch (Exception exception) {}

        return checkSides;
    }

    @ModifyArg(method = {"renderModel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModelRenderer;tesselateSmooth(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/BufferBuilder;ZLjava/util/Random;J)Z"))
    private boolean renderModelSmooth1(boolean checkSides) {
        try {
            if (SettingsMap.isOverrideMode()) {
                return false;
            }
        } catch (Exception exception) {}

        return checkSides;
    }

}
