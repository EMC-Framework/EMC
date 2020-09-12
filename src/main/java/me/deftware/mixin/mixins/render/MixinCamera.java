package me.deftware.mixin.mixins.render;

import me.deftware.mixin.imp.IMixinCamera;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderManager.class)
public class MixinCamera implements IMixinCamera {

	@Shadow
	private double renderPosX;

	@Shadow
	private double renderPosY;

	@Shadow
	private double renderPosZ;

	@Override
	public double getRenderPosX() {
		return renderPosX;
	}

	@Override
	public double getRenderPosY() {
		return renderPosY;
	}

	@Override
	public double getRenderPosZ() {
		return renderPosZ;
	}

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
