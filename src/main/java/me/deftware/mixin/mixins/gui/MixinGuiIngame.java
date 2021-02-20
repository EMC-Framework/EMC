package me.deftware.mixin.mixins.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import me.deftware.client.framework.event.events.EventAnimation;
import me.deftware.client.framework.event.events.EventRenderHotbar;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class MixinGuiIngame {

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SourceFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DestFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SourceFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DestFactor;)V"), cancellable = true)
    private void crosshairEvent(CallbackInfo ci) {
        if (!GameMap.INSTANCE.get(GameKeys.CROSSHAIR, true)) {
            GlStateManager.enableAlphaTest();
            ci.cancel();
        }
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void renderStatusEffectOverlay(CallbackInfo ci) {
        if (!GameMap.INSTANCE.get(GameKeys.EFFECT_OVERLAY, true))
            ci.cancel();
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

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPortalOverlay(float f, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Portal);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "method_1731", at = @At("HEAD"), cancellable = true)
    private void updateVignetteDarkness(Entity entity, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Vignette);
        event.broadcast();
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void renderVignetteOverlay(Entity entity, CallbackInfo ci) {
        EventAnimation event = new EventAnimation(EventAnimation.AnimationType.Vignette);
        event.broadcast();
        if (event.isCanceled()) {
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
