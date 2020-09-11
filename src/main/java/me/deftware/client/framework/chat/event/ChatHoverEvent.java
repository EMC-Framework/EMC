package me.deftware.client.framework.chat.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.util.text.event.HoverEvent;

import java.util.Objects;

/**
 * Currently only supports text!
 *
 * @author Deftware
 */
public @RequiredArgsConstructor class ChatHoverEvent {

	private @Getter final EventType eventType;
	private @Getter final ChatMessage message;
	private @Getter HoverEvent rawEvent = null; /* Internal only! */

	public HoverEvent toEvent() {
		return eventType == EventType.SHOW_TEXT ?
				new HoverEvent(eventType.getAction(), message.build()) : rawEvent;
	}

	public static ChatHoverEvent fromEvent(HoverEvent hoverEvent) {
		EventType type = EventType.UNSUPPORTED;
		for (EventType eventType : EventType.values()) {
			if (eventType.getAction().getCanonicalName().equalsIgnoreCase(hoverEvent.getAction().getCanonicalName())) {
				type = eventType;
				break;
			}
		}
		ChatMessage message = type == EventType.SHOW_TEXT ?
				new ChatMessage().fromText(Objects.requireNonNull(hoverEvent.getValue())) : null;
		ChatHoverEvent event = new ChatHoverEvent(type, message);
		event.rawEvent = hoverEvent;
		return event;
	}

	public @AllArgsConstructor enum EventType {

		SHOW_TEXT(HoverEvent.Action.SHOW_TEXT),
		UNSUPPORTED(HoverEvent.Action.SHOW_TEXT); // Dummy value

		private @Getter final HoverEvent.Action action;

	}

}
