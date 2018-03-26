package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventPostHotbar;
import me.deftware.client.framework.event.events.EventRender2D;
import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

	private float thePartialTicks = 0;

	@Inject(method = "renderGameOverlay", at = @At("HEAD"))
	private void renderGameOverlay(float partialTicks, CallbackInfo ci) {
		thePartialTicks = partialTicks;
	}

	@Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.enableBlend()V"))
	private void render2DEvent(CallbackInfo ci) {
		new EventRender2D(thePartialTicks).send();
	}

	@Inject(method = "renderAttackIndicator", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.enableAlpha()V"), cancellable = true)
	private void crosshairEvent(CallbackInfo ci) {
		if (!((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "CROSSHAIR", true))) {
			GlStateManager.enableAlpha();
			ci.cancel();
		}
	}

	@Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.color(FFFF)V", ordinal = 0))
	private void postHotbarEvent(CallbackInfo ci) {
		new EventPostHotbar().send();
	}

}
