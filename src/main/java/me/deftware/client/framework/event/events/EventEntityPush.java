package me.deftware.client.framework.event.events;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.event.Event;

/**
 * @author Deftware
 */
public class EventEntityPush extends Event {
	private final Entity entity;

	public EventEntityPush(final Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return this.entity;
	}
}
