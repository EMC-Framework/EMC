package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventRenderHotbar;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import net.minecraft.client.gui.GuiIngame;
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

    @Inject(method = "renderAttackIndicator", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.blendFuncSeparate(Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;)V"), cancellable = true)
    private void crosshairEvent(float partialTicks, CallbackInfo ci) {
        if (!((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "CROSSHAIR", true))) {
            GlStateManager.enableAlphaTest();
            ci.cancel();
        }
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void renderHotbar(float partialTicks, CallbackInfo ci) {
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
    private void renderPortalOverlay(float f, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Portal);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "func_212307_a", at = @At("HEAD"), cancellable = true)
    private void updateVignetteDarkness(Entity entity, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Vignette);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "func_212303_b", at = @At("HEAD"), cancellable = true)
    private void renderVignetteOverlay(Entity entity, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Vignette);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "func_212304_m", cancellable = true)
    private void getCameraPlayer(CallbackInfoReturnable<EntityPlayer> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(net.minecraft.client.Minecraft.getInstance().player);
            info.cancel();
        }
    }

}
