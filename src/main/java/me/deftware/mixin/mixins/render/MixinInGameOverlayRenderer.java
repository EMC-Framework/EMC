package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventAnimation;
import net.minecraft.client.renderer.FirstPersonRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonRenderer.class)
public class MixinInGameOverlayRenderer {

    @Unique
    private final static EventAnimation eventAnimation = new EventAnimation();

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void renderFireOverlay(CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Fire);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWaterOverlayTexture", at = @At("HEAD"), cancellable = true)
    private void renderUnderwaterOverlay(float float_1, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Underwater);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

}
