package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.mixin.imp.IMixinEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.PortalManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class MixinEntity implements IMixinEntity {

    @Shadow
    public boolean noClip;

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    protected abstract boolean getFlag(int id);

    @Shadow
    protected Vec3d movementMultiplier;

    @Shadow
    protected abstract void unsetRemoved();

    @Shadow public abstract float getYaw();

    @Shadow public abstract float getPitch();

    @Shadow private boolean glowing;

    @Shadow public abstract UUID getUuid();

    @Shadow @Nullable public PortalManager portalManager;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void changeLookDirection(double cursorX, double cursorY, CallbackInfo ci) {
        if ((Object) this == MinecraftClient.getInstance().player && CameraEntityMan.isActive()) {
            CameraEntityMan.fakePlayer.changeLookDirection(cursorX, cursorY);
            CameraEntityMan.fakePlayer.setHeadYaw(CameraEntityMan.fakePlayer.getYaw());
            ci.cancel();
        }
    }

    @Redirect(method = "updateMovementInFluid", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
            opcode = 182))
    private void applyFluidVelocity(Entity entity, Vec3d velocity) {
        if (entity == MinecraftClient.getInstance().player) {
            EventFluidVelocity event = new EventFluidVelocity((Vector3<Double>) velocity).broadcast();
            if (!event.isCanceled()) {
                entity.setVelocity((Vec3d) event.getVector3d());
            }
        } else {
            entity.setVelocity(velocity);
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    public void pushAwayFrom(Entity entity, CallbackInfo info) {
        if (((Object) this) == MinecraftClient.getInstance().player) {
            if (new EventEntityPush(ClientWorld.getClientWorld().getEntityByReference(entity)).broadcast().isCanceled()) {
                info.cancel();
            }
        }
    }

    @Redirect(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;noClip:Z", opcode = 180))
    private boolean noClipCheck(Entity self) {
        boolean noClipCheck = GameMap.INSTANCE.get(GameKeys.NOCLIP, false);
        if (self == MinecraftClient.getInstance().player) {
            return noClip || noClipCheck;
        }
        return self.noClip;
    }

    @Unique
    private final EventSlowdown slowdown = new EventSlowdown();

    @Inject(method = "slowMovement", at = @At(value = "TAIL"), cancellable = true)
    private void onSlowMovement(BlockState state, Vec3d multiplier, CallbackInfo ci) {
        if (((Object) this) == MinecraftClient.getInstance().player) {
            slowdown.create(EventSlowdown.SlowdownType.Web, 1);
            slowdown.broadcast();
            if (slowdown.isCanceled()) {
                Vec3d cobSlowness = new Vec3d(0.25D, 0.05000000074505806D, 0.25D);
                if (multiplier.x == cobSlowness.x && multiplier.y == cobSlowness.y && multiplier.z == cobSlowness.z) {
                    this.movementMultiplier = Vec3d.ZERO;
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "setVelocityClient", at = @At("HEAD"), cancellable = true)
    private void onSetVelocityClient(double x, double y, double z, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity == MinecraftClient.getInstance().player) {
            EventKnockback event = new EventKnockback(x, y, z).broadcast();
            if (!event.isCanceled()) {
                entity.setVelocity(event.getX(), event.getY(), event.getZ());
            }
            ci.cancel();
        }
    }

    @Redirect(
            method = "findCollisionsForMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;canCollide(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Z")
    )
    private static boolean onCollision$WorldBorder(WorldBorder instance, Entity entity, Box box) {
        if (GameMap.INSTANCE.get(GameKeys.IGNORE_WORLD_BORDER, false)) {
            return false;
        }
        return instance.canCollide(entity, box);
    }

    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void isGlowing(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(glowing);
    }

    @Override
    public boolean getAFlag(int id) {
        return getFlag(id);
    }

    @Override
    public void setInPortal(boolean inPortal) {
        portalManager.setInPortal(inPortal);
    }

    @Override
    public void removeRemovedReason() {
        unsetRemoved();
    }

}
