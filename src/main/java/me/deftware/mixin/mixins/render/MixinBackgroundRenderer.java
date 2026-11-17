package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventFogRender;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {

    @Unique
    private static final EventFogRender eventFogRender = new EventFogRender();

    @Inject(method = "setupFog", at = @At("RETURN"))
    private void applyFog(Camera camera, int viewDistance, DeltaTracker deltaTracker, float darkenWorldAmount,
                          ClientLevel level, CallbackInfoReturnable<FogData> cir) {
        eventFogRender.create(camera, FogRenderer.FogMode.WORLD, viewDistance * 16f, false);
        eventFogRender.broadcast();
        if (eventFogRender.isCanceled()) {
            FogData fogData = cir.getReturnValue();
            if (fogData != null) {
                fogData.environmentalStart = Float.MAX_VALUE;
                fogData.renderDistanceStart = Float.MAX_VALUE;
                fogData.environmentalEnd = Float.MAX_VALUE;
                fogData.renderDistanceEnd = Float.MAX_VALUE;
            }
        }
    }

}