package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventEntityRender;
import me.deftware.client.framework.world.World;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @Inject(method = "render(Lnet/minecraft/entity/Entity;DDDFFZ)V", at = @At("HEAD"), cancellable = true)
    private void render(Entity entity, double x, double y, double z, float yaw, float tickDelta, boolean forceHideHitbox, CallbackInfo ci) {
        if (!(entity instanceof ClientPlayerEntity) && entity instanceof LivingEntity) {
            EventEntityRender event = new EventEntityRender(World.getEntityById(entity.getEntityId()), x, y, z);
            event.broadcast();
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }

}