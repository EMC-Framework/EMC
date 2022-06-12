package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;

/**
 * Triggered when player sends a chat message
 */
public class EventChatSend extends Event {

	private String message;
	private final Type type;
	private final Class<?> sender;

	public EventChatSend(String message, Class<?> sender) {
		if (message.startsWith("/")) {
			this.type = Type.Command;
			message = message.substring(1);
		} else {
			this.type = Type.Message;
		}
		this.message = message;
		this.sender = sender;
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
