package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;

/**
 * Triggered when the mouse button is pressed
 */
public class EventMouseClick extends Event {
	private int button;

	public EventMouseClick(int button) {
		this.button = button;
	}

	public int getButton() {
		return button;
	}
}
