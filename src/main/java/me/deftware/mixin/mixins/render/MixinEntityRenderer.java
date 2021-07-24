package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.event.events.*;
import me.deftware.client.framework.helper.WindowHelper;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.shader.Shader;
import me.deftware.client.framework.render.batching.RenderStack;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import me.deftware.mixin.imp.IMixinEntityRenderer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(EntityRenderer.class)
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

    @Shadow @Final private net.minecraft.client.Minecraft mc;

    @Shadow private float farPlaneDistance;

    @Shadow protected abstract void orientCamera(float partialTicks);

    @Shadow protected abstract float getFOVModifier(float partialTicks, boolean useFOVSetting);

    @Shadow
    private ShaderGroup shaderGroup;

    @Shadow
    private boolean useShader;

    @Unique
    private final Consumer<Float> renderEvent = partialTicks -> new EventRender3D(partialTicks).broadcast();

    @Unique
    private final Consumer<Float> renderEventNoBobbing = partialTicks -> new EventRender3DNoBobbing(partialTicks).broadcast();

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void renderHand(float partialTicks, int pass, CallbackInfo ci) {
        if (!WindowHelper.isMinimized()) {
            renderEvent.accept(partialTicks);
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            if (this.mc.gameSettings.anaglyph) {
                GlStateManager.translate((float)(-(pass * 2 - 1)) * 0.07F, 0.0F, 0.0F);
            }
            if (this.cameraZoom != 1.0D) {
                GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
                GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
            }
            Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2);
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

    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", opcode = 180, target = "net/minecraft/client/gui/GuiIngame.renderGameOverlay(F)V"))
    private void onRender2D(GuiIngame guiIngame, float partialTicks) {
        guiIngame.renderGameOverlay(partialTicks);
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
            new EventRender2D(partialTicks).broadcast();
            // Render with custom matrix
            RenderStack.reloadCustomMatrix();
            RenderStack.setupGl();
            new EventMatrixRender(partialTicks).broadcast();
            RenderStack.restoreGl();
            RenderStack.reloadMinecraftMatrix();
        }
    }

    @Override
    public void loadCustomShader(MinecraftIdentifier location) {
        loadShader(location);
    }

    @Override
    public void loadShader(Shader shader) {
        if (shader == null) {
            this.shaderGroup = null;
            this.useShader = false;
            return;
        }
        if (this.shaderGroup != null)
            this.shaderGroup.deleteShaderGroup();
        if (shader.getShaderEffect() == null)
            shader.init();
        else
            shader.getShaderEffect().createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
        this.shaderGroup = shader.getShaderEffect();
        this.useShader = true;
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
