package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventKnockback;
import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.event.events.EventSneakingCheck;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.render.camera.GameCamera;
import me.deftware.mixin.imp.IMixinEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    protected boolean inNetherPortal;

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    protected abstract boolean getFlag(int id);

    @Shadow
    protected Vec3d movementMultiplier;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void changeLookDirection(double cursorX, double cursorY, CallbackInfo ci) {
        if ((Object) this == MinecraftClient.getInstance().player && GameCamera.isActive()) {
            GameCamera.fakePlayer.changeLookDirection(cursorX, cursorY);
            GameCamera.fakePlayer.setHeadYaw(GameCamera.fakePlayer.yaw);
            ci.cancel();
        }
    }

    @Inject(method = "getPose", at = @At(value = "TAIL"), cancellable = true)
    private void onGetPose(CallbackInfoReturnable<EntityPose> cir) {
        if ((boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "SWIMMING_MODE_OVERRIDE", false)) {
            cir.setReturnValue(EntityPose.SWIMMING);
        }
    }

    @Redirect(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;noClip:Z", opcode = 180))
    private boolean noClipCheck(Entity self) {
        boolean noClipCheck = (boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "NOCLIP", false);
        return noClip || noClipCheck && self instanceof ClientPlayerEntity;
    }


    @Inject(method = "slowMovement", at = @At(value = "TAIL"), cancellable = true)
    private void onSlowMovement(BlockState state, Vec3d multiplier, CallbackInfo ci) {
        EventSlowdown event = new EventSlowdown(EventSlowdown.SlowdownType.Web);
        event.broadcast();
        if (event.isCanceled()) {
            Vec3d cobSlowness = new Vec3d(0.25D, 0.05000000074505806D, 0.25D);
            if (multiplier.x == cobSlowness.x && multiplier.y == cobSlowness.y && multiplier.z == cobSlowness.z) {
                this.movementMultiplier = Vec3d.ZERO;
                ci.cancel();
            }
        }
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

    @Inject(method = "setVelocityClient", at = @At("HEAD"), cancellable = true)
    private void onSetVelocityClient(double x, double y, double z, CallbackInfo ci) {
        EventKnockback event = new EventKnockback(x, y, z);
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
        this.inNetherPortal = inPortal;
    }

}
