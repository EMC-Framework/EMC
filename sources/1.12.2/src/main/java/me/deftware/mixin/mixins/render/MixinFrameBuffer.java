package me.deftware.mixin.mixins.render;

import net.minecraft.client.renderer.OpenGlHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OpenGlHelper.class)
public class MixinFrameBuffer {

	@Inject(method = "initializeTextures", at = @At("RETURN"))
	private static void initializeTextures(CallbackInfo ci) {
		OpenGlHelper.GL_DEPTH_ATTACHMENT = 33306;
	}

	@ModifyVariable(method = "glRenderbufferStorage", at = @At("HEAD"), ordinal = 1)
	private static int glRenderbufferStorage(int internalFormat) {
		return 35056;
	}

}
