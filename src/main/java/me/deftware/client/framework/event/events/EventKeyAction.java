package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import org.lwjgl.glfw.GLFW;

/**
 * Triggered when some keyboard activity is received
 */
public class EventKeyAction extends Event {

    private int keyCode, action = 0, modifiers = 0;

    public EventKeyAction(int keyCode) {
        this.keyCode = GLFW.toGLFW.getOrDefault(keyCode, keyCode);
    }

    public int getKeyCode() {
        return keyCode;
    }

    /**
     * @see GLFW#GLFW_RELEASE
     * @see GLFW#GLFW_PRESS
     * @see GLFW#GLFW_REPEAT
     */
    public int getAction() {
        return action;
    }

    /**
     * @see GLFW#GLFW_MOD_SHIFT
     */
    public int getModifiers() {
        return modifiers;
    }

}
