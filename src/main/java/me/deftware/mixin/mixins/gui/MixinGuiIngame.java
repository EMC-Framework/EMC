package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventMatrixRender;
import me.deftware.client.framework.event.events.EventRender2D;
import me.deftware.client.framework.event.events.EventRender3D;
import me.deftware.client.framework.event.events.EventRender3DNoBobbing;
import me.deftware.client.framework.event.events.EventRenderHotbar;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.helper.WindowHelper;
import me.deftware.client.framework.render.batching.RenderStack;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Hud.class)
public class MixinGuiIngame {

    @Unique
    private static final Identifier PUMPKIN_BLUR = Identifier.withDefaultNamespace("misc/pumpkinblur")
            .withPath(string -> "textures/" + string + ".png");

    @Unique
    private final EventRender3D eventRender3D = new EventRender3D();

    @Unique
    private final EventRender3DNoBobbing eventRender3DNoBobbing = new EventRender3DNoBobbing();

    @Unique
    private final EventRender2D eventRender2D = new EventRender2D();

    @Unique
    private final EventMatrixRender eventMatrixRender = new EventMatrixRender();

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void onExtractRenderState(GuiGraphicsExtractor context, DeltaTracker tickCounter, CallbackInfo ci) {
        if (WindowHelper.isMinimized()) {
            return;
        }
        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);
        org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_STENCIL_TEST);
        Minecraft mc = Minecraft.getInstance();
        float tickDelta = tickCounter.getGameTimeDeltaPartialTick(true);
        if (mc.level != null && mc.gameRenderer != null && mc.gameRenderer.mainCamera().isInitialized()) {
            Matrix4f viewProj = mc.gameRenderer.mainCamera().getViewRotationProjectionMatrix(new Matrix4f());
            GLX glx3d = GLX.of3D(context, viewProj);
            eventRender3D.create(tickDelta);
            eventRender3D.setContext(glx3d);
            eventRender3D.broadcast();

            eventRender3DNoBobbing.create(tickDelta);
            eventRender3DNoBobbing.setContext(glx3d);
            eventRender3DNoBobbing.broadcast();
        }
        GLX glx = GLX.of(context);
        glx.color(1, 1, 1, 1);
        eventRender2D.setContext(glx);
        eventRender2D.create(tickDelta).broadcast();
        RenderStack.reloadCustomMatrix();
        RenderStack.setupGl();
        eventMatrixRender.setContext(glx);
        eventMatrixRender.create(tickDelta).broadcast();
        RenderStack.restoreGl();
        RenderStack.reloadMinecraftMatrix();
    }

    @Inject(method = "extractCrosshair", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
            ordinal = 0
    ), cancellable = true)
    private void crosshairEvent(CallbackInfo ci) {
        if (!GameMap.INSTANCE.get(GameKeys.CROSSHAIR, true)) {
            ci.cancel();
        }
    }

    @Inject(method = "extractEffects", at = @At("HEAD"), cancellable = true)
    private void renderStatusEffectOverlay(GuiGraphicsExtractor context, DeltaTracker tickCounter, CallbackInfo ci) {
        if (!GameMap.INSTANCE.get(GameKeys.EFFECT_OVERLAY, true))
            ci.cancel();
    }

    @Unique
    private final EventRenderHotbar eventRenderHotbar = new EventRenderHotbar();

    @Inject(method = "extractItemHotbar", at = @At("HEAD"))
    private void renderHotbar(GuiGraphicsExtractor context, DeltaTracker tickCounter, CallbackInfo ci) {
        eventRenderHotbar.setContext(GLX.of(context));
        eventRenderHotbar.broadcast();
    }

    @Unique
    private final EventAnimation eventAnimation = new EventAnimation();

    @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
    private void renderOverlay(GuiGraphicsExtractor context, Identifier texture, float opacity, CallbackInfo ci) {
        if (texture.equals(PUMPKIN_BLUR)) { // TODO: Verify this
            eventAnimation.create(EventAnimation.AnimationType.Pumpkin);
            eventAnimation.broadcast();
            if (eventAnimation.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPortalOverlay(GuiGraphicsExtractor context, float nauseaStrength, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Portal);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "updateVignetteBrightness", at = @At("HEAD"), cancellable = true)
    private void updateVignetteDarkness(Entity entity, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Vignette);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "extractVignette", at = @At("HEAD"), cancellable = true)
    private void renderVignetteOverlay(GuiGraphicsExtractor context, Entity entity, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Vignette);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "getCameraPlayer", cancellable = true)
    private void getCameraPlayer(CallbackInfoReturnable<Player> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(Minecraft.getInstance().player);
            info.cancel();
        }
    }

}
