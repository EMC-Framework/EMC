package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventAnimation;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinInGameOverlayRenderer {
    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void renderFireOverlay(CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Fire);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWaterOverlayTexture", at = @At("HEAD"), cancellable = true)
    private void renderUnderwaterOverlay(float float_1, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Underwater);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
