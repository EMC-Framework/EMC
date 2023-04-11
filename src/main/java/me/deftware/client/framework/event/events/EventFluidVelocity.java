package me.deftware.client.framework.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.event.Event;

/**
 * @author Deftware
 */
public @AllArgsConstructor class EventFluidVelocity extends Event {

	private @Getter @Setter Vector3<Double> vector3d;

}
