package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventStructureLocation;
import me.deftware.client.framework.item.ThrowData;
import net.minecraft.entity.EnderEyeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderEyeEntity.class)
public class MixinEnderEyeEntity {

    @Unique
    private static ThrowData firstThrow = null;

    @Unique
    private static ThrowData secondThrow = null;

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void onInit(CallbackInfo ci) {
        if (firstThrow != null && secondThrow != null) {
            firstThrow = null;
            secondThrow = null;
        }
    }

    @Inject(method = "moveTowards", at = @At("HEAD"))
    public void moveTowards(BlockPos pos, CallbackInfo ci) {
        EventStructureLocation event = new EventStructureLocation(pos.getX(), pos.getY(), pos.getZ(), EventStructureLocation.StructureType.Stronghold);
        event.broadcast();
    }

    @Inject(method = "setVelocityClient", at = @At("TAIL"))
    public void setVelocityClient(double x, double y, double z, CallbackInfo info) {
        EnderEyeEntity entity = (EnderEyeEntity) (Object) this;

        if (firstThrow == null) {
            firstThrow = new ThrowData(entity, (entity).getX(), (entity).getZ(), x, z);
            return;
        }
        if (firstThrow.sameEntity(entity)) {
            firstThrow.addVec(x, z);
            return;
        }

        if (secondThrow == null) {
            secondThrow = new ThrowData(entity, (entity).getX(), (entity).getZ(), x, z);
            return;
        }
        if (secondThrow.sameEntity(entity)) {
            secondThrow.addVec(x, z);
            return;
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EnderEyeEntity;setPos(DDD)V"))
    public void setPos(CallbackInfo info) {
        Vec3d vel = ((EnderEyeEntity)(Object)this).getVelocity();

        if (firstThrow != null && secondThrow != null && Math.abs(vel.x * vel.z) <= .0000001 && Math.abs(vel.y) != 0.0D) {
            EventStructureLocation event = new EventStructureLocation(firstThrow.calculateIntersection(secondThrow), EventStructureLocation.StructureType.Stronghold);
            event.broadcast();
        }
    }

}
