package me.deftware.client.framework.event.events;

import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.math.vector.Vector3d;

/**
 * @author Deftware
 */
public class EventFluidVelocity extends Event {
	private Vector3d vector3d;

	public EventFluidVelocity(final Vector3d vector3d) {
		this.vector3d = vector3d;
	}

	public Vector3d getVector3d() {
		return this.vector3d;
	}

	public void setVector3d(final Vector3d vector3d) {
		this.vector3d = vector3d;
	}
}
