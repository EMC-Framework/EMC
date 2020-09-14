package me.deftware.client.framework.event.events;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.event.Event;

public class EventEntityRender extends Event {
    private final Entity entity;
    private final double x;
    private final double y;
    private final double z;

    public EventEntityRender(Entity entity, double x, double y, double z) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}
