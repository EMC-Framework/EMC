package me.deftware.client.framework.input;

import me.deftware.client.framework.render.batching.RenderStack;
import me.deftware.mixin.imp.IMixinMinecraft;
import org.lwjgl.glfw.GLFW;

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
			((IMixinMinecraft) net.minecraft.client.Minecraft.getMinecraft()).doClickMouse();
		} else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			((IMixinMinecraft) net.minecraft.client.Minecraft.getMinecraft()).doRightClickMouse();
		} else if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			((IMixinMinecraft) net.minecraft.client.Minecraft.getMinecraft()).doMiddleClickMouse();
		}
	}

	public static boolean isButtonDown(int button) {
		return org.lwjgl.input.Mouse.getEventButton() == button;
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
		mouseY = org.lwjgl.input.Mouse.getY();
	}

}
