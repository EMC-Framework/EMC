package me.deftware.client.framework.input;

import me.deftware.client.framework.helper.WindowHelper;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.render.batching.RenderStack;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;
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

	private static final DoubleBuffer posX = BufferUtils.createDoubleBuffer(1), posY = BufferUtils.createDoubleBuffer(1);
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
		return GLFW.glfwGetMouseButton(MinecraftClient.getInstance().window.getHandle(), button) == 1;
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
		GLFW.glfwGetCursorPos(WindowHelper.getWindowHandle(), posX, posY);
		mouseX = posX.get();
		mouseY = posY.get();
		posX.clear();
		posY.clear();
	}

}
