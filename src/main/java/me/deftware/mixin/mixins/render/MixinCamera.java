package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventCameraClip;
import me.deftware.client.framework.render.camera.GameCamera;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Deftware, wagyourtail
 */
@Mixin(Camera.class)
public class MixinCamera implements GameCamera {

    @Inject(at = @At("HEAD"), cancellable = true, method = "entity")
    public void getFocusedEntity(CallbackInfoReturnable<Entity> info) {
        Minecraft mc = Minecraft.getInstance();
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(mc.player);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "isDetached")
    public void isThirdPerson(CallbackInfoReturnable<Boolean> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Unique
    private final EventCameraClip eventCameraClip = new EventCameraClip();

    @Inject(at = @At("HEAD"), cancellable = true, method = "getMaxZoom")
    public void clipToSpace(float camDistance, CallbackInfoReturnable<Float> info) {
        eventCameraClip.create(camDistance);
        eventCameraClip.broadcast();
        if (eventCameraClip.isCanceled()) {
            info.setReturnValue((float) eventCameraClip.getDistance());
            info.cancel();
        }
    }

    @Unique
    private final EventAnimation eventAnimation = new EventAnimation();

    @Inject(at = @At("HEAD"), cancellable = true, method = "getFluidInCamera")
    public void getSubmergedFluidState(CallbackInfoReturnable<FogType> info) {
        eventAnimation.create(EventAnimation.AnimationType.Underwater).broadcast();
        if (eventAnimation.isCanceled()) {
            // Assuming that cancel is in this case completely ignoring fog and not to keep old fog rendered
            info.setReturnValue(FogType.NONE);
        }
    }

    @Override
    public Vector3<Double> getCameraPosition() {
        return (Vector3<Double>) ((Camera) (Object) this).position();
    }

    @Override
    public float _getRotationPitch() {
        return ((Camera) (Object) this).xRot();
    }

    @Override
    public float _getRotationYaw() {
        return ((Camera) (Object) this).yRot();
    }

    @Override
    public double _getRenderPosX() {
        return ((Camera) (Object) this).position().x;
    }

    @Override
    public double _getRenderPosY() {
        return ((Camera) (Object) this).position().y;
    }

    @Override
    public double _getRenderPosZ() {
        return ((Camera) (Object) this).position().z;
    }

}
