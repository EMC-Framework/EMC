package me.deftware.mixin.mixins.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.deftware.client.framework.event.events.EventRenderHotbar;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.render.camera.entity.CameraEntityMan;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class MixinGuiIngame {

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"), cancellable = true)
    private void crosshairEvent(CallbackInfo ci) {
        if (!((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "CROSSHAIR", true))) {
            RenderSystem.enableAlphaTest();
            ci.cancel();
        }
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void renderHotbar(float partialTicks, MatrixStack matrixStack, CallbackInfo ci) {
        new EventRenderHotbar().broadcast();
    }

    @Inject(at = @At("HEAD"), method = "getCameraPlayer", cancellable = true)
    private void getCameraPlayer(CallbackInfoReturnable<PlayerEntity> info) {
        if (CameraEntityMan.isActive()) {
            info.setReturnValue(MinecraftClient.getInstance().player);
            info.cancel();
        }
    }

}
