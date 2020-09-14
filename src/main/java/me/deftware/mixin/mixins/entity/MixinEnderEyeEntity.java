package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.EventStructureLocation;
import me.deftware.client.framework.item.ThrowData;
import me.deftware.client.framework.math.position.DoubleBlockPosition;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityEnderEye.class)
public class MixinEnderEyeEntity {

    @Unique
    private static ThrowData firstThrow = null;

    @Unique
    private static ThrowData secondThrow = null;

    /* FIXME
    @Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At("HEAD"))
    public void onInit(CallbackInfo ci) {
        if (firstThrow != null && secondThrow != null) {
            firstThrow = null;
            secondThrow = null;
        }
    }*/

    @Inject(method = "moveTowards", at = @At("HEAD"))
    public void moveTowards(BlockPos pos, CallbackInfo ci) {
        EventStructureLocation event = new EventStructureLocation(DoubleBlockPosition.fromMinecraftBlockPos(pos), EventStructureLocation.StructureType.Stronghold);
        event.broadcast();
    }

    @Inject(method = "setVelocity", at = @At("TAIL"))
    public void setVelocityClient(double x, double y, double z, CallbackInfo info) {
        EntityEnderEye entity = (EntityEnderEye) (Object) this;

        if (firstThrow == null) {
            firstThrow = new ThrowData(entity, (entity).posX, (entity).posZ, x, z);
            return;
        }
        if (firstThrow.sameEntity(entity)) {
            firstThrow.addVec(x, z);
            return;
        }

        if (secondThrow == null) {
            secondThrow = new ThrowData(entity, (entity).posX, (entity).posZ, x, z);
            return;
        }
        if (secondThrow.sameEntity(entity)) {
            secondThrow.addVec(x, z);
            return;
        }
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setPosition(DDD)V"))
    public void setPos(CallbackInfo info) {
        double velX = ((EntityEnderEye)(Object)this).motionX, velZ = ((EntityEnderEye)(Object)this).motionZ, velY = ((EntityEnderEye)(Object)this).motionY;

        if (firstThrow != null && secondThrow != null && Math.abs(velX * velZ) <= .0000001 && Math.abs(velY) != 0.0D) {
            EventStructureLocation event = new EventStructureLocation(DoubleBlockPosition.fromMinecraftBlockPos(firstThrow.calculateIntersection(secondThrow)), EventStructureLocation.StructureType.Stronghold);
            event.broadcast();
        }
    }

}
