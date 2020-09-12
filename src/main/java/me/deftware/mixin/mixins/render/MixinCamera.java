package me.deftware.mixin.mixins.render;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class MixinCamera {

    /* FIXME
    @Inject(at = @At("HEAD"), cancellable = true, method = "getFocusedEntity")
    public void getFocusedEntity(CallbackInfoReturnable<Entity> info) {
        Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(mc.player);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "isThirdPerson")
    public void isThirdPerson(CallbackInfoReturnable<Boolean> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "method_19318")
    public void clipToSpace(double camDistance, CallbackInfoReturnable<Double> info) {
        EventCameraClip event = new EventCameraClip(camDistance);
        event.broadcast();
        if (event.isCanceled()) {
            info.setReturnValue(event.getDistance());
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "getSubmergedFluidState")
    public void getSubmergedFluidState(CallbackInfoReturnable<FluidState> info) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Underwater);
        event.broadcast();
        if (event.isCanceled()) {
            // Assuming that cancel is in this case completely ignoring fog and not to keep old fog rendered
            info.setReturnValue(Fluids.EMPTY.getDefaultState());
            info.cancel();
        }
    }*/

}
