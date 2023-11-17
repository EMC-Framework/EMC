package org.lwjgl.glfw;

public abstract class GLFWCharCallback implements GLFWCharCallbackI {

	public abstract void invoke(long window, int codepoint);

}

