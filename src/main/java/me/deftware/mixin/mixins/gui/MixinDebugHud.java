package me.deftware.mixin.mixins.gui;

import net.minecraft.client.gui.GuiOverlayDebug;
import me.deftware.client.framework.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;

@Mixin(GuiOverlayDebug.class)
public class MixinDebugHud {

	@Inject(method = "call", at = @At("TAIL"), cancellable = true)
	protected void retrieveLeftText(CallbackInfoReturnable<List<String>> cir) {
		cir.setReturnValue(getModifiedList(cir.getReturnValue()));
	}

	@Inject(method = "getDebugInfoRight", at = @At("TAIL"), cancellable = true)
	protected void retrieveRightText(CallbackInfoReturnable<List<String>> cir) {
		cir.setReturnValue(getModifiedList(cir.getReturnValue()));
	}

	private List<String> getModifiedList(List<String> stringData) {
		for (Function<List<String>, List<String>> modifier : Minecraft.getMinecraftGame().getDebugModifiers()) {
			stringData = modifier.apply(stringData);
		}
		return stringData;
	}

}
