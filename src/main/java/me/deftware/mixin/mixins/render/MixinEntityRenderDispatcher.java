package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventEntityRender;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderManager.class)
public class MixinEntityRenderDispatcher {

    @Unique
    private final EventEntityRender eventEntityRender = new EventEntityRender();

    @Inject(method = "renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V", at = @At("HEAD"), cancellable = true)
    private void render(Entity entity, double x, double y, double z, float yaw, float tickDelta, boolean forceHideHitbox, CallbackInfo ci) {
        if (!(entity instanceof EntityPlayerSP) && entity instanceof EntityLiving) {
            eventEntityRender.create(ClientWorld.getClientWorld().getEntityByReference(entity), x, y, z);
            eventEntityRender.broadcast();
            if (eventEntityRender.isCanceled()) {
                ci.cancel();
            }
        }
    }

}
