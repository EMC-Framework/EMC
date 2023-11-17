package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.world.ClientWorld;
import net.minecraft.entity.Entity;

/**
 * Triggered when player model is being rendered.
 * It does not include the model drawn in players' inventory
 */
public class EventRenderPlayerModel extends Event {

	private me.deftware.client.framework.entity.Entity entity;
	private boolean shouldRender = false;

	public EventRenderPlayerModel create(Entity entity) {
		this.shouldRender = false;
		this.entity = ClientWorld.getClientWorld().getEntityByReference(entity);
		return this;
	}

	public me.deftware.client.framework.entity.Entity getEntity() {
		return entity;
	}

	public void setEntity(me.deftware.client.framework.entity.Entity entity) {
		this.entity = entity;
	}

	public boolean isShouldRender() {
		return shouldRender;
	}

	public void setShouldRender(boolean shouldRender) {
		this.shouldRender = shouldRender;
	}

}
