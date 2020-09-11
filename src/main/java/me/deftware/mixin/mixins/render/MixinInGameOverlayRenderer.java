package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventAnimation;
import net.minecraft.client.render.FirstPersonRenderer;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonRenderer.class)
public class MixinInGameOverlayRenderer {
    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    private void renderInWallOverlay(Sprite sprite, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Wall);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private void renderFireOverlay(CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Fire);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWaterOverlay", at = @At("HEAD"), cancellable = true)
    private void renderUnderwaterOverlay(float tickDelta, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Underwater);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
