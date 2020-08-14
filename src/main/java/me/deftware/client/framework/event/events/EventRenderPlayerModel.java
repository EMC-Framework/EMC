package me.deftware.client.framework.event.events;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.Event;
import net.minecraft.entity.Entity;

/**
 * Triggered when player model is being rendered.
 * It does not include the model drawn in players' inventory
 */
public class EventRenderPlayerModel extends Event {
	private boolean shouldRender = false;
	private final ChatMessage name;

	public EventRenderPlayerModel(Entity entity) {
		this.name = new ChatMessage().fromText(entity.getName());
	}

	public boolean isShouldRender() {
		return this.shouldRender;
	}

	public void setShouldRender(final boolean shouldRender) {
		this.shouldRender = shouldRender;
	}

	public ChatMessage getName() {
		return this.name;
	}
}
