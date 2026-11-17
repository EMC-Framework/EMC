package me.deftware.mixin.mixins.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.deftware.client.framework.event.events.EventHurtcam;
import me.deftware.client.framework.render.shader.Shader;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinEntityRenderer implements IMixinEntityRenderer {

    @Shadow
    @Final
    private Camera mainCamera;

    @Unique
    private final EventHurtcam eventHurtcam = new EventHurtcam();

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(CameraRenderState cameraRenderState, PoseStack stack, CallbackInfo ci) {
        eventHurtcam.setCanceled(false);
        eventHurtcam.broadcast();
        if (eventHurtcam.isCanceled()) {
            ci.cancel();
        }
    }

    @Unique
    private Shader shader;

    @Override
    public void loadShader(Shader shader) {
        if (shader != null && !shader.isLoaded()) {
            shader.init();
        }
        this.shader = shader;
    }

    @Inject(method = "resize", at = @At("HEAD"))
    private void onResized(int width, int height, CallbackInfo ci) {
        if (shader != null) {
            shader.getFramebuffer().resize(width, height);
        }
    }

    @Override
    public float getFovMultiplier() {
        return this.mainCamera.fovModifier;
    }

    @Override
    public void updateFovMultiplier(float newFov) {
        this.mainCamera.fovModifier = this.mainCamera.oldFovModifier = newFov;
    }

}
