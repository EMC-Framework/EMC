package org.lwjgl.glfw;

import org.lwjgl.system.CallbackI;
import org.lwjgl.system.Callback.Descriptor;

/**
 * Compatibility shim for LWJGL GLFW char callback interface on Minecraft 26.3 (SDL3).
 */
@FunctionalInterface
public interface GLFWCharCallbackI extends CallbackI {

    void invoke(long window, int codepoint);

    @Override
    default Descriptor getDescriptor() {
        return null;
    }

    @Override
    default void callback(long ret, long args) {
        // No-op: invoked directly via invoke(window, codepoint) from SDL3 text input
    }

}
