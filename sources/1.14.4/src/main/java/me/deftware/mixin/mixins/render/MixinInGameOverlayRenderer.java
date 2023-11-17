package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventAnimation;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class MixinInGameOverlayRenderer {

    @Unique
    private final static EventAnimation eventAnimation = new EventAnimation();

    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    private void renderInWallOverlay(Sprite sprite, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Wall);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private void renderFireOverlay(CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Fire);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWaterOverlay", at = @At("HEAD"), cancellable = true)
    private void renderUnderwaterOverlay(float tickDelta, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Underwater);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }
}
