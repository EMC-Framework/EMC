package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventKnockback;
import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.event.events.EventSneakingCheck;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.mixin.imp.IMixinEntity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
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
    public boolean isInWeb;

    @Shadow
    protected boolean inPortal;

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract boolean isPassenger();

    @Shadow
    public abstract AxisAlignedBB getBoundingBox();

    @Shadow
    protected abstract boolean getFlag(int int_1);

    @Redirect(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;noClip:Z", opcode = 180))
    private boolean noClipCheck(Entity self) {
        boolean noClipCheck = (boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "NOCLIP", false);
        return noClip || noClipCheck && self instanceof EntityPlayerSP;
    }

    @Inject(method = "isEntityInsideOpaqueBlock", at = @At(value = "HEAD"), cancellable = true)
    public void isInWall(CallbackInfoReturnable<Boolean> cir) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Wall);
        event.broadcast();
        if (event.isCanceled()) {
            cir.setReturnValue(false);
        }
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
    public boolean getAFlag(int flag) {
        return getFlag(flag);
    }

    @Override
    public void setInPortal(boolean inPortal) {
        this.inPortal = inPortal;
    }

}
