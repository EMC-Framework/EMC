package me.deftware.mixin.mixins.input;

import me.deftware.client.framework.event.events.EventCharacter;
import me.deftware.client.framework.event.events.EventKeyAction;
import me.deftware.client.framework.event.events.EventKeyActionRaw;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboardListener {

    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKeyEventRaw(long windowPointer, int keyCode, int scanCode, int action, int modifiers, CallbackInfo ci) {
        var minecraft = MinecraftClient.getInstance();
        if (windowPointer == minecraft.getWindow().getHandle()) {
            new EventKeyActionRaw(keyCode, action, modifiers).broadcast();
            if (minecraft.currentScreen == null && keyCode >= 32 && keyCode <= 348) {
                if (action != GLFW.GLFW_RELEASE) {
                    new EventKeyAction(keyCode, action, modifiers).broadcast();
                }
            }
        }
    }

    @Inject(method = "onChar", at = @At("HEAD"))
    private void onCharEvent(long windowPointer, int codePoint, int modifiers, CallbackInfo ci) {
        if (windowPointer == MinecraftClient.getInstance().getWindow().getHandle() && MinecraftClient.getInstance().currentScreen == null && codePoint >= 32 && codePoint <= 348) {
            if (Character.charCount(codePoint) == 1) {
                new EventCharacter((char) codePoint, modifiers).broadcast();
            } else {
                for (char character : Character.toChars(codePoint)) {
                    new EventCharacter(character, modifiers).broadcast();
                }
            }
        }
    }

}
