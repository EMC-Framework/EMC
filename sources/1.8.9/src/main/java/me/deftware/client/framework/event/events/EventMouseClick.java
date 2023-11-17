package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import org.lwjgl.glfw.GLFW;

/**
 * Triggered when the mouse button is pressed
 */
public class EventMouseClick extends Event {

	private int button, action, modifiers;

	public EventMouseClick(int button, boolean action) {
		this.button = button;
		this.action = action ? 1 : 0;
		this.modifiers = 0;
	}

	/**
	 * @see GLFW#GLFW_MOUSE_BUTTON_LEFT
	 * @see GLFW#GLFW_MOUSE_BUTTON_RIGHT
	 * @see GLFW#GLFW_MOUSE_BUTTON_MIDDLE
	 */
	public int getButton() {
		return button;
	}

	/**
	 * @see GLFW#GLFW_RELEASE
	 * @see GLFW#GLFW_PRESS
	 */
	public int getAction() {
		return action;
	}

	public int getModifiers() {
		return modifiers;
	}

}
