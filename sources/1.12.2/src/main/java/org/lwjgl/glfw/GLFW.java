package org.lwjgl.glfw;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Used to translate static keys between lwjgl2 to lwjgl3
 */
public class GLFW {

	public static List<GLFWCharCallbackI> callbacks = new ArrayList<>();

	public static HashMap<Integer, Integer> toGLFW = new HashMap<Integer, Integer>() {{
		// Custom keys
		put(Keyboard.KEY_ESCAPE, 256);
		put(Keyboard.KEY_BACK, 259);
		put(Keyboard.KEY_TAB, 258);
		put(Keyboard.KEY_SPACE, 32);
		put(Keyboard.KEY_RETURN, 257);
		put(Keyboard.KEY_MINUS, 45);
		put(Keyboard.KEY_SLASH, 47);
		put(Keyboard.KEY_1, 49);
		put(Keyboard.KEY_RSHIFT, 344);
		put(Keyboard.KEY_F3, 292);
		put(Keyboard.KEY_PERIOD, 46);

		// Arrow keys
		put(Keyboard.KEY_UP, 265);
		put(Keyboard.KEY_DOWN, 264);
		put(Keyboard.KEY_LEFT, 263);
		put(Keyboard.KEY_RIGHT, 262);

		// Normal keys
		put(Keyboard.KEY_V, 86);
		put(Keyboard.KEY_C, 67);
		put(Keyboard.KEY_X, 88);

		// Modifier keys
		put(Keyboard.KEY_LCONTROL, 341);
		put(Keyboard.KEY_RCONTROL, 345);
		put(Keyboard.KEY_LSHIFT, 340);
	}};

	public static HashMap<Integer, Integer> fromGLFW = new HashMap<Integer, Integer>() {{
		// Custom keys
		put(256, Keyboard.KEY_ESCAPE);
		put(259, Keyboard.KEY_BACK);
		put(258, Keyboard.KEY_TAB);
		put(32, Keyboard.KEY_SPACE);
		put(257, Keyboard.KEY_RETURN);
		put(45, Keyboard.KEY_MINUS);
		put(47, Keyboard.KEY_SLASH);
		put(49, Keyboard.KEY_1);
		put(344, Keyboard.KEY_RSHIFT);
		put(292, Keyboard.KEY_F3);
		put(43, Keyboard.KEY_PERIOD);

		// Arrow keys
		put(265, Keyboard.KEY_UP);
		put(264, Keyboard.KEY_DOWN);
		put(263, Keyboard.KEY_LEFT);
		put(262, Keyboard.KEY_RIGHT);

		// Normal keys
		put(86, Keyboard.KEY_V);
		put(67, Keyboard.KEY_C);
		put(88, Keyboard.KEY_X);

		// Modifier keys
		put(341, Keyboard.KEY_LCONTROL);
		put(345, Keyboard.KEY_RCONTROL);
		put(340, Keyboard.KEY_LSHIFT);
	}};

	/*
		Custom keys
	 */
	public static int GLFW_KEY_ESCAPE = Keyboard.KEY_ESCAPE,
			GLFW_KEY_BACKSPACE = Keyboard.KEY_BACK,
			GLFW_KEY_TAB = Keyboard.KEY_TAB,
			GLFW_KEY_SPACE = Keyboard.KEY_SPACE,
			GLFW_KEY_ENTER = Keyboard.KEY_NUMPADENTER,
			GLFW_KEY_MINUS = Keyboard.KEY_MINUS,
			GLFW_KEY_SLASH = Keyboard.KEY_SLASH,
			GLFW_KEY_1 = Keyboard.KEY_1,
			GLFW_KEY_RIGHT_SHIFT = Keyboard.KEY_RSHIFT,
			GLFW_KEY_UNKNOWN = -1;

	/*
		Mod
	 */
	public static int GLFW_MOD_SHIFT = Keyboard.KEY_RSHIFT;

	/*
		Arrow keys
	 */
	public static int GLFW_KEY_UP = Keyboard.KEY_UP,
			GLFW_KEY_DOWN = Keyboard.KEY_DOWN,
			GLFW_KEY_LEFT = Keyboard.KEY_LEFT,
			GLFW_KEY_RIGHT = Keyboard.KEY_RIGHT;

	/*
		Mouse buttons
	 */
	public static int GLFW_MOUSE_BUTTON_LEFT = 0,
			GLFW_MOUSE_BUTTON_RIGHT = 1,
			GLFW_MOUSE_BUTTON_MIDDLE = 2;

	/*
		Cursors
	 */
	public static int GLFW_HRESIZE_CURSOR = 0x36005,
			GLFW_VRESIZE_CURSOR = 0x36006,
			GLFW_ARROW_CURSOR = 0x36001;

	/*
		Functions
	 */

	public static int glfwGetKeyScancode(int key) {
		return 0;
	}

	public static String glfwGetKeyName(int key, int scancode) {
		return Keyboard.getKeyName(fromGLFW.getOrDefault(key, key));
	}

	public static void glfwSetCursor(long window, long cursor) {
		// TODO
	}

	public static long glfwCreateStandardCursor(int shape) {
		// TODO
		return 0;
	}

	public static GLFWCharCallback glfwSetCharCallback(long handle, GLFWCharCallbackI callback) {
		callbacks.add(callback);
		return (GLFWCharCallback) callback;
	}

}
