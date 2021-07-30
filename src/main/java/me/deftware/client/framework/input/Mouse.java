package me.deftware.client.framework.input;

import me.deftware.client.framework.render.batching.RenderStack;
import me.deftware.client.framework.minecraft.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * @author Deftware
 */
public class Mouse {

	/**
	 * The raw mouse X and Y coordinates, without Minecraft scaling applied
	 */
	public static double mouseX = 0, mouseY = 0;

	private final static ArrayList<BiConsumer<Double, Double>> scrollCallbacks = new ArrayList<>();

	/**
	 * @see GLFW#GLFW_MOUSE_BUTTON_LEFT
	 * @see GLFW#GLFW_MOUSE_BUTTON_RIGHT
	 * @see GLFW#GLFW_MOUSE_BUTTON_MIDDLE
	 */
	public static void clickMouse(int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			Minecraft.getMinecraftGame().doClickMouse();
		} else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			Minecraft.getMinecraftGame().doRightClickMouse();
		} else if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			Minecraft.getMinecraftGame().doMiddleClickMouse();
		}
	}

	public static boolean isButtonDown(int button) {
		return org.lwjgl.input.Mouse.isButtonDown(button);
	}

	public static double getMouseX() {
		return mouseX / RenderStack.getScale();
	}

	public static double getMouseY() {
		return mouseY / RenderStack.getScale();
	}

	public static void registerScrollHook(BiConsumer<Double, Double> cb) {
		scrollCallbacks.add(cb);
	}

	public static void onScroll(double x, double y) {
		scrollCallbacks.forEach(cb -> cb.accept(x, y));
	}

	public static void updateMousePosition() {
		mouseX = org.lwjgl.input.Mouse.getX();
		// Mouse Y is inverted in lwjgl 2
		mouseY = Display.getHeight() - org.lwjgl.input.Mouse.getY();
	}

}
