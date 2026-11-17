package me.deftware.mixin.mixins.render;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import me.deftware.client.framework.render.WorldEntityRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LevelRenderer.class)
public abstract class MixinWorldRenderer implements WorldEntityRenderer {

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Unique
    @Getter
    private final List<Statue> statues = new ArrayList<>();

    @Inject(method = "submitEntities", at = @At("RETURN"))
    private void onRenderStatues(PoseStack matrices, LevelRenderState renderState, SubmitNodeCollector collector, CallbackInfo ci) {
        if (this.statues.isEmpty()) {
            return;
        }
        double camX = renderState.cameraRenderState.pos.x;
        double camY = renderState.cameraRenderState.pos.y;
        double camZ = renderState.cameraRenderState.pos.z;
        float tickDelta = renderState.cameraRenderState.cameraEntityPartialTicks;
        for (Statue statue : this.statues) {
            EntityRenderState entityState = this.entityRenderDispatcher.extractEntity(statue.getEntity().getMinecraftEntity(), tickDelta);
            this.entityRenderDispatcher.submit(
                    entityState,
                    renderState.cameraRenderState,
                    statue.getPosition().getX() - camX,
                    statue.getPosition().getY() - camY,
                    statue.getPosition().getZ() - camZ,
                    matrices,
                    collector
            );
        }
    }

}
