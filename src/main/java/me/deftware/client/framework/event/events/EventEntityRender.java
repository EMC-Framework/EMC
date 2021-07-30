package me.deftware.client.framework.event.events;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.event.Event;

public class EventEntityRender extends Event {
    private Entity entity;
    private double x;
    private double y;
    private double z;

    public EventEntityRender create(Entity entity, double x, double y, double z) {
        setCanceled(false);
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
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
