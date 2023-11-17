package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;

/**
 * Triggered when player sends a chat message
 */
public class EventChatSend extends Event {

	private String message;
	private final Type type;
	private final Class<?> sender;

	public EventChatSend(String message, Class<?> sender, Type type) {
		this.message = message;
		this.sender = sender;
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Class<?> getSender() {
		return sender;
	}

	public enum Type {
		Message, Command
	}

}
