package me.deftware.mixin.mixins;

import com.mojang.blaze3d.platform.GLX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GLX.class)
public class MixinGlStateManager {

	@Inject(method = "init", at = @At("RETURN"))
	private static void initFramebufferSupport(CallbackInfo ci) {
		GLX.GL_DEPTH_ATTACHMENT = 33306;
	}

	@ModifyVariable(method = "glRenderbufferStorage", at = @At("HEAD"), ordinal = 1)
	private static int renderbufferStorage(int internalFormat) {
		return 35056;
	}

}
