package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;

/**
 * Triggered when player sends a chat message
 */
public class EventChatSend extends Event {
	private String message;
	private boolean dispatch = false;

	public EventChatSend(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public boolean isDispatch() {
		return this.dispatch;
	}

	public void setDispatch(final boolean dispatch) {
		this.dispatch = dispatch;
	}
}
