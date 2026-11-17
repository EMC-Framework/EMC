package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.function.Function;

@Mixin(DebugScreenOverlay.class)
public class MixinDebugHud {

	@ModifyVariable(method = "extractLines", at = @At("HEAD"), argsOnly = true)
	private List<String> modifyDebugLines(List<String> lines) {
		return getModifiedList(lines);
	}

	private List<String> getModifiedList(List<String> stringData) {
		for (Function<List<String>, List<String>> modifier : Minecraft.getMinecraftGame().getDebugModifiers()) {
			stringData = modifier.apply(stringData);
		}
		return stringData;
	}

}
