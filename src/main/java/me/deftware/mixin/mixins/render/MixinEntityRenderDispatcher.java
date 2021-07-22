package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventEntityRender;
import me.deftware.client.framework.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderManager.class)
public class MixinEntityRenderDispatcher {

    @Inject(method = "renderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V", at = @At("HEAD"), cancellable = true)
    private void render(Entity entity, double x, double y, double z, float yaw, float tickDelta, boolean forceHideHitbox, CallbackInfo ci) {
        if (!(entity instanceof EntityPlayerSP) && entity instanceof EntityLiving) {
            EventEntityRender event = new EventEntityRender(World.getEntityById(entity.getEntityId()), x, y, z);
            event.broadcast();
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }

}
