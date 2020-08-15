package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import org.lwjgl.glfw.GLFW;

/**
 * Triggered when some keyboard activity is received
 */
public class EventKeyAction extends Event {

    private int keyCode;

    public EventKeyAction(int keyCode) {
        this.keyCode = GLFW.toGLFW.getOrDefault(keyCode, keyCode);
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getAction() {
        return 0;
    }

    public int getModifiers() {
        return 0;
    }

}
