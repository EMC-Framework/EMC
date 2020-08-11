package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventNametagRender;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinRender<T extends Entity> {

    @Inject(method = "renderLabel(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V", at = @At("HEAD"), cancellable = true)
    private void renderEntityLabel(T entity_1, String text, double x, double y, double z, int maxDistance, CallbackInfo ci) {
        EventNametagRender event = new EventNametagRender(entity_1);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

}
