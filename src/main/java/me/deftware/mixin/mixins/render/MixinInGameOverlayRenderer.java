package me.deftware.mixin.mixins.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.deftware.client.framework.event.events.EventAnimation;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class MixinInGameOverlayRenderer {

    @Unique
    private final static EventAnimation eventAnimation = new EventAnimation();

    @Inject(method = "submitBlockSprite", at = @At("HEAD"), cancellable = true)
    private static void renderInWallOverlay(Identifier texture, float minX, float minY, float maxX, float maxY, PoseStack matrices, SubmitNodeCollector collector, int light, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Wall);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true)
    private static void renderFireOverlay(PoseStack matrixStack, SubmitNodeCollector collector, TextureAtlasSprite sprite, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Fire);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
    private static void renderUnderwaterOverlay(PlayerRenderState.WaterOverlay waterOverlay, PoseStack matrices, SubmitNodeCollector collector, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Underwater);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

}
