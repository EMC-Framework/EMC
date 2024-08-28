package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventFogRender;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {

    @Unique
    private static final EventFogRender eventFogRender = new EventFogRender();

    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance,
                                 boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
        eventFogRender.create(camera, fogType, viewDistance, thickenFog);
        eventFogRender.broadcast();
        if (eventFogRender.isCanceled()) {
            cir.cancel();
        }
    }

}