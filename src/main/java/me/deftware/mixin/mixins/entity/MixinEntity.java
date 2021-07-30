package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.mixin.imp.IMixinEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class MixinEntity implements IMixinEntity {

    @Shadow
    public boolean noClip;

    @Shadow
    protected boolean onGround;

    @Shadow
    public float rotationPitch;

    @Shadow
    public float rotationYaw;

    @Shadow
    protected boolean inPortal;

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    protected abstract boolean getFlag(int id);

    @Shadow protected boolean isInWeb;

    @Shadow public double posX;

    @Redirect(method = "moveEntity", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F", opcode = 180))
    private float modifyStepHeight(Entity self) {
        return self == Minecraft.getMinecraft().thePlayer ? 0.6f : self.stepHeight;
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "setAngles", at = @At("HEAD"), cancellable = true)
    public void changeLookDirection(float cursorX, float cursorY, CallbackInfo ci) {
        if ((Object) this == Minecraft.getMinecraft().thePlayer && CameraEntityMan.isActive()) {
            CameraEntityMan.fakePlayer.setAngles(cursorX, cursorY);
            CameraEntityMan.fakePlayer.setRotationYawHead(CameraEntityMan.fakePlayer.rotationYaw);
            ci.cancel();
        }
    }

    @Inject(method = "setEntityId", at = @At("HEAD"))
    public void onSetEntityId(int id, CallbackInfo ci) {
        ClientWorld clientWorld = ClientWorld.getClientWorld();
        int localId = ((Entity) (Object) this).getEntityId();
        if (clientWorld.getEntities().containsKey(localId)) {
            if (id != localId) {
                // Update ID in client world
                clientWorld.getEntities().put(id, clientWorld.getEntities().remove(localId));
            }
        }
    }

    @Unique
    private final EventAnimation eventAnimation = new EventAnimation();

    @Inject(method = "isEntityInsideOpaqueBlock", at = @At(value = "HEAD"), cancellable = true)
    public void isInWall(CallbackInfoReturnable<Boolean> cir) {
        eventAnimation.create(EventAnimation.AnimationType.Wall);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            cir.setReturnValue(false);
        }
    }

    /* FIXME
    @Redirect(method = "handleFluidAcceleration", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
            opcode = 182))
    private void applyFluidVelocity(Entity entity, Vec3d velocity) {
        if (entity == net.minecraft.client.Minecraft.getMinecraft().thePlayer) {
            EventFluidVelocity event = new EventFluidVelocity(new Vector3d(velocity)).broadcast();
            if (!event.isCanceled()) {
                entity.setVelocity(event.getVector3d().getMinecraftVector());
            }
        } else {
            entity.setVelocity(velocity);
        }
    }*/

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void pushAwayFrom(Entity entity, CallbackInfo info) {
        if (((Object) this) == net.minecraft.client.Minecraft.getMinecraft().thePlayer) {
            if (new EventEntityPush(ClientWorld.getClientWorld().getEntityByReference(entity)).broadcast().isCanceled()) {
                info.cancel();
            }
        }
    }

    @Redirect(method = "moveEntity", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;noClip:Z", opcode = 180))
    private boolean noClipCheck(Entity self) {
        boolean noClipCheck = GameMap.INSTANCE.get(GameKeys.NOCLIP, false);
        return noClip || noClipCheck && self instanceof EntityPlayerSP;
    }

    @Unique
    private final EventSlowdown slowdown = new EventSlowdown();

    @Redirect(method = "moveEntity", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;isInWeb:Z", opcode = 180))
    private boolean webCheck(Entity self) {
        slowdown.create(EventSlowdown.SlowdownType.Web, 1);
        slowdown.broadcast();
        if (slowdown.isCanceled()) {
            isInWeb = false;
        }
        return isInWeb;
    }

    @Redirect(method = "moveEntity", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.isSneaking()Z", opcode = 180, ordinal = 0))
    private boolean sneakingCheck(Entity self) {
        EventSneakingCheck event = new EventSneakingCheck(isSneaking());
        event.broadcast();
        if (event.isSneaking()) {
            return true;
        }
        return getFlag(1);
    }

    @Inject(method = "setVelocity", at = @At("HEAD"), cancellable = true)
    public void setVelocityClient(double x, double y, double z, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity == Minecraft.getMinecraft().thePlayer) {
            EventKnockback event = new EventKnockback(x, y, z).broadcast();
            if (event.isCanceled()) {
                ci.cancel();
            }
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

