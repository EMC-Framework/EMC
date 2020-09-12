package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventFogRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {

    @Final
    @Shadow
    private GameRenderer entityRenderer;

    @Final
    @Shadow
    private Minecraft mc;

    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private void applyFog(boolean blackIn, CallbackInfo ci) {
        EventFogRender event = new EventFogRender();
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

}