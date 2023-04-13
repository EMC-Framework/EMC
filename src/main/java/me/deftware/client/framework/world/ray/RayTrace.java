package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;

/**
 * @author Deftware
 */
public abstract class RayTrace<T> {

    protected final Vector3<Double> start, end;
    protected final RayProfile profile;

    public RayTrace(Vector3<Double> start, Vector3<Double> end, RayProfile profile) {
        this.start = start;
        this.end = end;
        this.profile = profile;
    }

    public abstract T run(Entity entity);

}
