package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;

/**
 * Triggered when player sends a chat message
 */
public class EventChatSend extends Event {
	private String message;
	private boolean dispatch = false;
	private final Class<?> sender;

	public EventChatSend(String message, Class<?> sender) {
		this.message = message;
		this.sender = sender;
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

	public Class<?> getSender() {
		return this.sender;
	}
}
