package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventStructureLocation;
import me.deftware.client.framework.item.ThrowData;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EyeOfEnder.class)
public class MixinEnderEyeEntity {

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    public void onInit(CallbackInfo ci) {
        ThrowData.resetThrows();
    }

    @Inject(method = "signalTo", at = @At("HEAD"))
    public void moveTowards(Vec3 pos, CallbackInfo ci) {
        EventStructureLocation event = new EventStructureLocation(pos.x, pos.y, pos.z, EventStructureLocation.StructureType.Stronghold);
        event.broadcast();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/EyeOfEnder;setPos(Lnet/minecraft/world/phys/Vec3;)V"))
    public void setPos(CallbackInfo info) {
        ThrowData.checkIntersection((EyeOfEnder) (Object) this);
    }

}
