package me.deftware.mixin.mixins.input;

import me.deftware.client.framework.event.events.EventKeyAction;
import me.deftware.client.framework.event.events.EventMouseClick;
import me.deftware.client.framework.input.MinecraftKeyBind;
import me.deftware.client.framework.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHelper.class)
public class MixinMouseListener {
	
	@Shadow
	private boolean mouseGrabbed;
	
	// target the best place for no screen or overlay open, similar to {@Link MixinKeyboardListener}
	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHelper;mouseGrabbed:Z"), method = "mouseButtonCallback")
	public void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		// if mouse is already locked and button sanity check
		if (mouseGrabbed && button >= 0 && button <= 7 && action == GLFW.GLFW_PRESS) {
			new EventKeyAction(button, action, mods).broadcast();
		}
	}

	@Inject(method = "mouseButtonCallback", at = @At("HEAD"))
	private void mouseButtonCallback(long windowPointer, int button, int action, int modifiers, CallbackInfo ci) {
		if (windowPointer != Minecraft.getInstance().mainWindow.getHandle() || Minecraft.getInstance().currentScreen != null) {
			return;
		}
		new EventMouseClick(button, action, modifiers).broadcast();
	}

	@Inject(method = "scrollCallback", at = @At("HEAD"))
	private void scrollCallback(long windowPointer, double xoffset, double yoffset, CallbackInfo ci) {
		if (windowPointer != Minecraft.getInstance().mainWindow.getHandle()) {
			return;
		}
		Mouse.onScroll(xoffset, yoffset);
	}

}
