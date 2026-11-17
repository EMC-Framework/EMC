package me.deftware.mixin.mixins.entity;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.mixin.imp.IMixinEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PortalProcessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
    public boolean noPhysics;

    @Shadow
    public abstract boolean isShiftKeyDown();

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    protected abstract boolean getSharedFlag(int id);

    @Shadow
    protected Vec3 stuckSpeedMultiplier;

    @Shadow
    protected abstract void unsetRemoved();

    @Shadow public abstract float getYRot();

    @Shadow public abstract float getXRot();

    @Shadow private boolean hasGlowingTag;

    @Shadow public abstract UUID getUUID();

    @Shadow @Nullable public PortalProcessor portalProcess;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void changeLookDirection(double cursorX, double cursorY, CallbackInfo ci) {
        if ((Object) this == Minecraft.getInstance().player && CameraEntityMan.isActive()) {
            CameraEntityMan.fakePlayer.turn(cursorX, cursorY);
            CameraEntityMan.fakePlayer.setYHeadRot(CameraEntityMan.fakePlayer.getYRot());
            ci.cancel();
        }
    }

    @Redirect(method = "updateFluidInteraction", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityFluidInteraction;applyCurrentTo(Lnet/minecraft/tags/TagKey;Lnet/minecraft/world/entity/Entity;D)V"))
    private void applyFluidVelocity(net.minecraft.world.entity.EntityFluidInteraction instance, net.minecraft.tags.TagKey<net.minecraft.world.level.material.Fluid> tag, Entity entity, double speed) {
        if (entity == Minecraft.getInstance().player) {
            EventFluidVelocity event = new EventFluidVelocity((Vector3<Double>) (Object) entity.getDeltaMovement()).broadcast();
            if (!event.isCanceled()) {
                instance.applyCurrentTo(tag, entity, speed);
            }
        } else {
            instance.applyCurrentTo(tag, entity, speed);
        }
    }

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void pushAwayFrom(Entity entity, CallbackInfo info) {
        if (((Object) this) == Minecraft.getInstance().player) {
            if (new EventEntityPush(ClientWorld.getClientWorld().getEntityByReference(entity)).broadcast().isCanceled()) {
                info.cancel();
            }
        }
    }

    @Redirect(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;noPhysics:Z", opcode = 180))
    private boolean noClipCheck(Entity self) {
        boolean noClipCheck = GameMap.INSTANCE.get(GameKeys.NOCLIP, false);
        if (self == Minecraft.getInstance().player) {
            return noPhysics || noClipCheck;
        }
        return self.noPhysics;
    }

    @Unique
    private final EventSlowdown slowdown = new EventSlowdown();

    @Inject(method = "makeStuckInBlock", at = @At(value = "TAIL"), cancellable = true)
    private void onSlowMovement(BlockState state, Vec3 multiplier, CallbackInfo ci) {
        if (((Object) this) == Minecraft.getInstance().player) {
            slowdown.create(EventSlowdown.SlowdownType.Web, 1);
            slowdown.broadcast();
            if (slowdown.isCanceled()) {
                Vec3 cobSlowness = new Vec3(0.25D, 0.05000000074505806D, 0.25D);
                if (multiplier.x == cobSlowness.x && multiplier.y == cobSlowness.y && multiplier.z == cobSlowness.z) {
                    this.stuckSpeedMultiplier = Vec3.ZERO;
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "lerpMotion", at = @At("HEAD"), cancellable = true)
    private void onSetVelocityClient(Vec3 movement, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof net.minecraft.world.entity.projectile.EyeOfEnder enderEye) {
            me.deftware.client.framework.item.ThrowData.trackMotion(enderEye, movement.x, movement.z);
        }
        if (entity == Minecraft.getInstance().player) {
            EventKnockback event = new EventKnockback(movement.x, movement.y, movement.z).broadcast();
            if (!event.isCanceled()) {
                entity.setDeltaMovement(event.getX(), event.getY(), event.getZ());
            }
            ci.cancel();
        }
    }

    @Redirect(
            method = "collectCollidersIgnoringWorldBorder(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/border/WorldBorder;isInsideCloseToBorder(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z")
    )
    private static boolean onCollision$WorldBorder(WorldBorder instance, Entity entity, AABB box) {
        if (GameMap.INSTANCE.get(GameKeys.IGNORE_WORLD_BORDER, false)) {
            return false;
        }
        return instance.isInsideCloseToBorder(entity, box);
    }

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void isGlowing(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(hasGlowingTag);
    }

    @Override
    public boolean getAFlag(int id) {
        return getSharedFlag(id);
    }

    @Override
    public void setInPortal(boolean inPortal) {
        portalProcess.setAsInsidePortalThisTick(inPortal);
    }

    @Override
    public void removeRemovedReason() {
        unsetRemoved();
    }

}
