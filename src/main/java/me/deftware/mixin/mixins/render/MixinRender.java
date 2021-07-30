package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventNametagRender;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public class MixinRender<T extends Entity> {

    @Unique
    private final EventNametagRender eventNametagRender = new EventNametagRender();

    @Inject(method = "renderLivingLabel", at = @At("HEAD"), cancellable = true)
    private void renderEntityLabel(T entity, String text, double x, double y, double z, int maxDistance, CallbackInfo ci) {
        eventNametagRender.create(entity);
        eventNametagRender.broadcast();
        if (eventNametagRender.isCanceled()) {
            ci.cancel();
        }
    }


}
