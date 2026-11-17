package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/**
 * Compatibility shim for LWJGL GLFW char callback base class on Minecraft 26.3 (SDL3).
 */
public abstract class GLFWCharCallback extends Callback implements GLFWCharCallbackI {

    protected GLFWCharCallback() {
        super(1L);
    }

    @Override
    public long address() {
        return 0L;
    }

    @Override
    public void free() {
        // No native upcall handle allocated
    }

}
