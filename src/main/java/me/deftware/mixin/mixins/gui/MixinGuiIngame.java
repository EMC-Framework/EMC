package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventRenderHotbar;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import me.deftware.client.framework.render.gl.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class MixinGuiIngame {

    @Shadow
    @Final
    private static Identifier PUMPKIN_BLUR;

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"), cancellable = true)
    private void crosshairEvent(CallbackInfo ci) {
        if (!GameMap.INSTANCE.get(GameKeys.CROSSHAIR, true)) {
            // RenderSystem.enableAlphaTest();
            ci.cancel();
        }
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void renderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!GameMap.INSTANCE.get(GameKeys.EFFECT_OVERLAY, true))
            ci.cancel();
    }

    @Unique
    private final EventRenderHotbar eventRenderHotbar = new EventRenderHotbar();

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void renderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        eventRenderHotbar.setContext(GLX.of(context));
        eventRenderHotbar.broadcast();
    }

    @Unique
    private final EventAnimation eventAnimation = new EventAnimation();

    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private void renderOverlay(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        if (texture == PUMPKIN_BLUR) { // TODO: Verify this
            eventAnimation.create(EventAnimation.AnimationType.Pumpkin);
            eventAnimation.broadcast();
            if (eventAnimation.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPortalOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Portal);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "updateVignetteDarkness", at = @At("HEAD"), cancellable = true)
    private void updateVignetteDarkness(Entity entity, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Vignette);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void renderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo ci) {
        eventAnimation.create(EventAnimation.AnimationType.Vignette);
        eventAnimation.broadcast();
        if (eventAnimation.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "getCameraPlayer", cancellable = true)
    private void getCameraPlayer(CallbackInfoReturnable<PlayerEntity> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(MinecraftClient.getInstance().player);
            info.cancel();
        }
    }

}
