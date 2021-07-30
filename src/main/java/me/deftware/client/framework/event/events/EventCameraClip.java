package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;

public class EventCameraClip extends Event {

    private double distance;

    public EventCameraClip create(double desiredDistance) {
        setCanceled(false);
        this.distance = desiredDistance;
        return this;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return this.distance;
    }
}
