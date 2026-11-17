package me.deftware.mixin.mixins.input;

import me.deftware.client.framework.event.events.EventCharacter;
import me.deftware.client.framework.event.events.EventKeyAction;
import me.deftware.client.framework.event.events.EventKeyActionRaw;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardListener {

    @Inject(method = "keyPress", at = @At("HEAD"))
    private void onKeyEventRaw(long windowPointer, int action, KeyEvent keyEvent, CallbackInfo ci) {
        var minecraft = Minecraft.getInstance();
        if (windowPointer == 0L || windowPointer == minecraft.getWindow().handle()) {
            int keyCode = GLFW.sdlScancodeToGlfwKey(keyEvent.key(), keyEvent.keycode());
            int glfwAction = GLFW.sdlActionToGlfwAction(action);
            int modifiers = GLFW.sdlModsToGlfwMods(keyEvent.modifiers());
            new EventKeyActionRaw(keyCode, glfwAction, modifiers).broadcast();
            if (minecraft.gui.screen() == null && keyCode >= 32 && keyCode <= 348) {
                if (glfwAction != GLFW.GLFW_RELEASE) {
                    new EventKeyAction(keyCode, glfwAction, modifiers).broadcast();
                }
            }
        }
    }

    @Inject(method = "charTyped", at = @At("HEAD"))
    private void onCharEvent(long windowPointer, CharacterEvent characterEvent, CallbackInfo ci) {
        int codePoint = characterEvent.codepoint();
        var minecraft = Minecraft.getInstance();
        if ((windowPointer == 0L || windowPointer == minecraft.getWindow().handle()) && minecraft.gui.screen() == null && codePoint >= 32 && codePoint <= 348) {
            if (Character.charCount(codePoint) == 1) {
                new EventCharacter((char) codePoint, 0).broadcast();
            } else {
                for (char character : Character.toChars(codePoint)) {
                    new EventCharacter(character, 0).broadcast();
                }
            }
        }
    }

}
