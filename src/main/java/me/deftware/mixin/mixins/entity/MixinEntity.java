package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.math.vector.Vector3d;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.mixin.imp.IMixinEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class MixinEntity implements IMixinEntity {

    @Shadow
    public boolean noClip;

    @Shadow
    protected boolean onGround;

    @Shadow
    public float pitch;

    @Shadow
    public float yaw;

    @Shadow
    protected boolean inPortal;

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    protected abstract boolean getFlag(int id);

    @Shadow protected boolean isInWeb;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "rotateTowards", at = @At("HEAD"), cancellable = true)
    public void changeLookDirection(double cursorX, double cursorY, CallbackInfo ci) {
        if ((Object) this == net.minecraft.client.Minecraft.getInstance().player && CameraEntityMan.isActive()) {
            CameraEntityMan.fakePlayer.rotateTowards(cursorX, cursorY);
            CameraEntityMan.fakePlayer.setRotationYawHead(CameraEntityMan.fakePlayer.rotationYaw);
            ci.cancel();
        }
    }

    @Redirect(method = "handleFluidAcceleration", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
            opcode = 182))
    private void applyFluidVelocity(Entity entity, Vec3d velocity) {
        if (entity == net.minecraft.client.Minecraft.getInstance().player) {
            EventFluidVelocity event = new EventFluidVelocity(new Vector3d(velocity)).broadcast();
            if (!event.isCanceled()) {
                entity.setVelocity(event.getVector3d().getMinecraftVector());
            }
        } else {
            entity.setVelocity(velocity);
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    public void pushAwayFrom(Entity entity, CallbackInfo info) {
        if (((Object) this) == net.minecraft.client.Minecraft.getInstance().player) {
            if (new EventEntityPush(me.deftware.client.framework.entity.Entity.newInstance(entity)).broadcast().isCanceled()) {
                info.cancel();
            }
        }
    }

    @Inject(method = "getPose", at = @At(value = "TAIL"), cancellable = true)
    private void onGetPose(CallbackInfoReturnable<EntityPose> cir) {
        if (((Object) this) == net.minecraft.client.Minecraft.getInstance().player) {
            if ((boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "SWIMMING_MODE_OVERRIDE", false)) {
                cir.setReturnValue(EntityPose.SWIMMING);
            }
        }
    }

    @Redirect(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;noClip:Z", opcode = 180))
    private boolean noClipCheck(Entity self) {
        boolean noClipCheck = (boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "NOCLIP", false);
        return (self instanceof ClientPlayerEntity && self == net.minecraft.client.Minecraft.getInstance().player) && (noClip || noClipCheck);
    }

    @Redirect(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;isInWeb:Z", opcode = 180))
    private boolean webCheck(Entity self) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Web);
        event.broadcast();
        if (event.isCanceled()) {
            isInWeb = false;
        }
        return isInWeb;
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.isSneaking()Z", opcode = 180, ordinal = 0))
    private boolean sneakingCheck(Entity self) {
        EventSneakingCheck event = new EventSneakingCheck(isSneaking());
        event.broadcast();
        if (event.isSneaking()) {
            return true;
        }
        return getFlag(1);
    }

    @Inject(method = "setVelocity", at = @At("HEAD"), cancellable = true)
    public void setVelocityClient(double double_1, double double_2, double double_3, CallbackInfo ci) {
        EventKnockback event = new EventKnockback(double_1, double_2, double_3);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Override
    public boolean getAFlag(int id) {
        return getFlag(id);
    }

    @Override
    public void setInPortal(boolean inPortal) {
        this.inPortal = inPortal;
    }

}

