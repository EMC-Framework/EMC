package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventRenderHotbar;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderStreamIndicator", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.enableAlpha()V"), cancellable = true)
    private void crosshairEvent(ScaledResolution scaledRes, CallbackInfo ci) {
        if (!GameMap.INSTANCE.get(GameKeys.CROSSHAIR, true)) {
            GlStateManager.enableAlpha();
            ci.cancel();
        }
    }

    /*@Inject(method = "renderPotionEffects", at = @At("HEAD"), cancellable = true)
    private void renderStatusEffectOverlay(CallbackInfo ci) {
        if (!GameMap.INSTANCE.get(GameKeys.EFFECT_OVERLAY, true))
            ci.cancel();
    }*/

    @Inject(method = "renderPlayerStats", at = @At("HEAD"))
    private void postHotbarEvent(ScaledResolution sr, CallbackInfo ci) {
        new EventRenderHotbar().broadcast();
    }

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPumpkinOverlay(CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Pumpkin);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPortal", at = @At("HEAD"), cancellable = true)
    private void renderPortalOverlay(float f, ScaledResolution res, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Portal);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    /*@Inject(method = "func_212307_a", at = @At("HEAD"), cancellable = true) FIXME
    private void updateVignetteDarkness(Entity entity, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Vignette);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }*/

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    private void renderVignetteOverlay(float lightlevel, ScaledResolution res, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Vignette);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    /*@Inject(at = @At("HEAD"), method = "func_212304_m", cancellable = true) FIXME
    private void getCameraPlayer(CallbackInfoReturnable<EntityPlayer> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(net.minecraft.client.Minecraft.getMinecraft().thePlayer);
            info.cancel();
        }
    }*/

}
