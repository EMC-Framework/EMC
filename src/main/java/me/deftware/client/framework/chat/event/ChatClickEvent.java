package me.deftware.client.framework.chat.event;

import net.minecraft.util.text.event.ClickEvent;

/**
 * @author Deftware
 */
public class ChatClickEvent {
	private final EventType eventType;
	private String data;

	@SuppressWarnings("ConstantConditions")
	public ClickEvent toEvent() {
		return new ClickEvent(eventType.toAction(), data);
	}

	public static ChatClickEvent fromEvent(ClickEvent event) {
		EventType type = EventType.OPEN_URL;
		for (EventType eventType : EventType.values()) {
			if (eventType.getName().equalsIgnoreCase(event.getAction().getCanonicalName())) {
				type = eventType;
				break;
			}
		}
		return new ChatClickEvent(type, event.getValue());
	}


	public enum EventType {
		OPEN_URL("open_url"), OPEN_FILE("open_file"), RUN_COMMAND("run_command"), SUGGEST_COMMAND("suggest_command"), CHANGE_PAGE("change_page"), COPY_TO_CLIPBOARD("copy_to_clipboard");
		private final String name;

		public ClickEvent.Action toAction() {
			for (ClickEvent.Action action : ClickEvent.Action.values()) {
				if (action.getCanonicalName().equalsIgnoreCase(name)) {
					return action;
				}
			}
			return null;
		}

		private EventType(final String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	public ChatClickEvent(final EventType eventType, final String data) {
		this.eventType = eventType;
		this.data = data;
	}

	public EventType getEventType() {
		return this.eventType;
	}

	public void setData(final String data) {
		this.data = data;
	}
}
