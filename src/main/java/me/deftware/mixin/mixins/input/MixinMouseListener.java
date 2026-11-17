package me.deftware.mixin.mixins.input;

import me.deftware.client.framework.event.events.EventKeyAction;
import me.deftware.client.framework.event.events.EventMouseClick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseListener {
    
    @Shadow
    private boolean mouseGrabbed;
    
    // target the best place for no screen or overlay open, similar to {@Link MixinKeyboardListener}
    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHandler;mouseGrabbed:Z"), method = "onButton")
    public void onMouseButton(long window, MouseButtonInfo buttonInfo, int action, CallbackInfo ci) {
        int button = GLFW.sdlButtonToGlfwButton(buttonInfo.button());
        int glfwAction = GLFW.sdlActionToGlfwAction(action);
        int mods = GLFW.sdlModsToGlfwMods(buttonInfo.modifiers());
        // if mouse is already locked and button sanity check
        // and GLFW_PRESS check
        if (mouseGrabbed && button >= 0 && button <= 7 && glfwAction == GLFW.GLFW_PRESS) {
            new EventKeyAction(button, glfwAction, mods).broadcast();
        }
    }
    
    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void mouseButtonCallback(long windowPointer, MouseButtonInfo buttonInfo, int action, CallbackInfo ci) {
        var mc = Minecraft.getInstance();
        if (windowPointer == 0L || windowPointer == mc.getWindow().handle() || mc.gui.screen() != null) {
            int button = GLFW.sdlButtonToGlfwButton(buttonInfo.button());
            int glfwAction = GLFW.sdlActionToGlfwAction(action);
            int mods = GLFW.sdlModsToGlfwMods(buttonInfo.modifiers());
            EventMouseClick event = new EventMouseClick(button, glfwAction, mods).broadcast();
            if (event.isCanceled())
                ci.cancel();
        }
    }
    
    @Inject(method = "onScroll", at = @At("HEAD"))
    private void scrollCallback(long windowPointer, double horizontal, double vertical, CallbackInfo ci) {
        if (windowPointer == 0L || windowPointer == Minecraft.getInstance().getWindow().handle()) {
            me.deftware.client.framework.input.Mouse.onScroll(horizontal, vertical);
        }
    }
}
