package me.deftware.client.framework.event.events;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.event.Event;

/**
 * @author Deftware
 */
public class EventFluidVelocity extends Event {

	private Vector3<Double> vector3d;

	public EventFluidVelocity(Vector3<Double> vector3d) {
		this.vector3d = vector3d;
	}

	public Vector3<Double> getVector3d() {
		return this.vector3d;
	}

	public void setVector3d(Vector3<Double> vector3d) {
		this.vector3d = vector3d;
	}

}
