package me.deftware.client.framework.chat.event;

import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.util.text.event.HoverEvent;
import java.util.Objects;

/**
 * Currently only supports text!

 *

 * @author Deftware
 */
public class ChatHoverEvent {
	private final EventType eventType;
	private final ChatMessage message;
	private HoverEvent rawEvent = null; /* Internal only! */

	public HoverEvent toEvent() {
		return eventType == EventType.SHOW_TEXT ? new HoverEvent(eventType.getAction(), message.build()) : rawEvent;
	}

	public static ChatHoverEvent fromEvent(HoverEvent hoverEvent) {
		EventType type = EventType.UNSUPPORTED;
		for (EventType eventType : EventType.values()) {
			if (eventType.getAction().getCanonicalName().equalsIgnoreCase(hoverEvent.getAction().getCanonicalName())) {
				type = eventType;
				break;
			}
		}
		ChatMessage message = type == EventType.SHOW_TEXT ? new ChatMessage().fromText(Objects.requireNonNull(hoverEvent.getValue()), false) : null;
		ChatHoverEvent event = new ChatHoverEvent(type, message);
		event.rawEvent = hoverEvent;
		return event;
	}


	public enum EventType {
		SHOW_TEXT(HoverEvent.Action.SHOW_TEXT), UNSUPPORTED(HoverEvent.Action.SHOW_TEXT);
		// Dummy value
		private final HoverEvent.Action action;

		private EventType(final HoverEvent.Action action) {
			this.action = action;
		}

		public HoverEvent.Action getAction() {
			return this.action;
		}
	}

	public ChatHoverEvent(final EventType eventType, final ChatMessage message) {
		this.eventType = eventType;
		this.message = message;
	}

	public EventType getEventType() {
		return this.eventType;
	}

	public ChatMessage getMessage() {
		return this.message;
	}

	public HoverEvent getRawEvent() {
		return this.rawEvent;
	}
}
