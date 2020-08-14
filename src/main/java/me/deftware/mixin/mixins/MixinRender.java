package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventNametagRender;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public class MixinRender<T extends Entity> {

    @Inject(method = "renderLivingLabel", at = @At("HEAD"), cancellable = true)
    private void renderEntityLabel(T entity_1, String text, double x, double y, double z, int maxDistance, CallbackInfo ci) {
        EventNametagRender event = new EventNametagRender(entity_1);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }


}
