package me.deftware.mixin.mixins;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class MixinRenderSystem {

	@Inject(method = "enableDepthTest", at = @At(value = "HEAD"), cancellable = true)
	private static void renderWorld(CallbackInfo ci) {
		if (!((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "WORLD_DEPTH", true))) {
			ci.cancel();
		}
	}

}
