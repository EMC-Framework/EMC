package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventWeather;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Inject(method = "method_22713", at = @At("HEAD"), cancellable = true)
    private void renderRain(Camera camera, CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void renderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

}
