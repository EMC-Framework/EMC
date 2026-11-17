package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventEntityRender;
import me.deftware.client.framework.world.ClientWorld;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @Unique
    private final EventEntityRender eventEntityRender = new EventEntityRender();

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void render(E entity, Frustum frustum, double x, double y, double z, float tickDelta, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof LocalPlayer) && entity instanceof LivingEntity) {
            eventEntityRender.create(ClientWorld.getClientWorld().getEntityByReference(entity), x, y, z);
            eventEntityRender.broadcast();
            if (eventEntityRender.isCanceled()) {
                cir.setReturnValue(false);
            }
        }
    }

}
