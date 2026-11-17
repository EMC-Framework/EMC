package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.global.types.BlockPropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.mixin.shared.BlockManagement;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelBlockRenderer.class)
public abstract class MixinBlockModelRenderer {

    @Unique
    private BlockPos pos;

    @Inject(method = "tesselateBlock", at = @At("HEAD"), cancellable = true)
    private void onTesselateBlockHead(BlockQuadOutput output, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState state, BlockStateModel model, long seed, CallbackInfo ci) {
        this.pos = pos;
        BlockPropertyManager blockProperties = Bootstrap.blockProperties;
        if (!blockProperties.isActive()) {
            BlockManagement.isRenderingOpacityBlock.set(false);
            return;
        }
        int id = BuiltInRegistries.BLOCK.getId(state.getBlock());
        boolean isTarget = blockProperties.contains(id) && blockProperties.get(id).isRender();
        if (!isTarget && !blockProperties.isOpacityMode()) {
            BlockManagement.isRenderingOpacityBlock.set(false);
            ci.cancel();
            return;
        }
        BlockManagement.isRenderingOpacityBlock.set(blockProperties.isOpacityMode() && !isTarget);
    }

    @Inject(method = "tesselateBlock", at = @At("RETURN"))
    private void onTesselateBlockReturn(BlockQuadOutput output, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState state, BlockStateModel model, long seed, CallbackInfo ci) {
        BlockManagement.isRenderingOpacityBlock.set(false);
    }

    @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
    private void onShouldRenderFace(BlockAndTintGetter level, BlockState state, Direction direction, BlockPos neighborPos, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = this.pos != null ? this.pos : neighborPos.relative(direction.getOpposite());
        var result = BlockManagement.shouldDrawSide(state, level, blockPos, direction);
        if (result.isPresent()) {
            cir.setReturnValue(result.get());
        }
    }

}
