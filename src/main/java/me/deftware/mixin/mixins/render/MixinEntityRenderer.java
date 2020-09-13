package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.helper.GlStateHelper;
import me.deftware.client.framework.helper.WindowHelper;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public abstract class MixinEntityRenderer implements IMixinEntityRenderer {

    @Shadow
    private float fovModifierHand;

    @Shadow
    private float fovModifierHandPrev;

    @Shadow
    protected abstract void loadShader(ResourceLocation p_loadShader_1_);

    @Shadow private double cameraZoom;

    @Shadow private double cameraYaw;

    @Shadow private double cameraPitch;

    @Shadow protected abstract double getFOVModifier(float partialTicks, boolean useFOVSetting);

    @Shadow @Final private net.minecraft.client.Minecraft mc;

    @Shadow private float farPlaneDistance;

    @Shadow protected abstract void orientCamera(float partialTicks);

    @Unique
    private final Consumer<Float> renderEvent = partialTicks -> new EventRender3D(partialTicks).broadcast();

    @Unique
    private final Consumer<Float> renderEventNoBobbing = partialTicks -> new EventRender3DNoBobbing(partialTicks).broadcast();

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void renderHand(float partialTicks, CallbackInfo ci) {
        if (!WindowHelper.isMinimized()) {
            renderEvent.accept(partialTicks);
            // Setup matrix
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            if (this.cameraZoom != 1.0D) {
                GlStateManager.translatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
                GlStateManager.scaled(this.cameraZoom, this.cameraZoom, 1.0D);
            }
            GlStateManager.multMatrixf(Matrix4f.perspective(this.getFOVModifier(partialTicks, true), (float)this.mc.mainWindow.getFramebufferWidth() / (float)this.mc.mainWindow.getFramebufferHeight(), 0.05F, this.farPlaneDistance * MathHelper.SQRT_2));
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            // Set camera
            this.orientCamera(partialTicks);
            renderEventNoBobbing.accept(partialTicks);
            GlStateManager.enableLighting();
        }
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        EventHurtcam event = new EventHurtcam();
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "updateCameraAndRender(FJZ)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiIngame.renderGameOverlay(F)V"))
    private void onRender2D(CallbackInfo cb) {
        if (!WindowHelper.isMinimized()) {
            // Chat queue
            Runnable operation = ChatHud.getChatMessageQueue().poll();
            if (operation != null) {
                operation.run();
            }
            // Other actions
            operation = Minecraft.RENDER_THREAD.poll();
            if (operation != null) {
                operation.run();
            }
            new EventRender2D(0f).broadcast();
        }
    }

    @Override
    public void loadCustomShader(MinecraftIdentifier location) {
        loadShader(location);
    }

    @Override
    public float getFovMultiplier() {
        return fovModifierHand;
    }

    @Override
    public void updateFovMultiplier(float newFov) {
        fovModifierHandPrev = newFov;
        fovModifierHand = newFov;
    }

    @Inject(method = "addRainParticles", at = @At("HEAD"), cancellable = true)
    private void addRainParticles(CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderRainSnow", at = @At("HEAD"), cancellable = true)
    private void renderRainSnow(float partialTicks, CallbackInfo ci) {
        EventWeather event = new EventWeather(EventWeather.WeatherType.Rain);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "updateCameraAndRender(FJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isSpectator()Z", opcode = 180))
    public boolean isSpectator(EntityPlayerSP entityPlayerSP) {
        return entityPlayerSP.isSpectator() || CameraEntityMan.isActive();
    }

}
