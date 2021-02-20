package me.deftware.mixin.mixins.render;

import net.minecraft.client.renderer.GlStateManager;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class MixinRenderSystem {

	@Inject(method = "enableDepthTest", at = @At(value = "HEAD"), cancellable = true)
	private static void renderWorld(CallbackInfo ci) {
		if (!GameMap.INSTANCE.get(GameKeys.WORLD_DEPTH, true))
			ci.cancel();
	}

}
