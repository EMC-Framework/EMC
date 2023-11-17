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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public class MixinEntityRenderDispatcher {

    @Unique
    private final EventEntityRender eventEntityRender = new EventEntityRender();

    @Inject(method = "doRenderEntity", at = @At("HEAD"), cancellable = true)
    private void render(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof EntityPlayerSP) && entity instanceof EntityLiving) {
            eventEntityRender.create(ClientWorld.getClientWorld().getEntityByReference(entity), x, y, z);
            eventEntityRender.broadcast();
            if (eventEntityRender.isCanceled()) {
                cir.cancel();
            }
        }
    }

}
