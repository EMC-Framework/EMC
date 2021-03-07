package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.vector.Vector3d;

/**
 * @author Deftware
 */
public abstract class RayTrace<T> {

    protected final Vector3d start, end;
    protected final RayProfile profile;

    public RayTrace(Vector3d start, Vector3d end, RayProfile profile) {
        this.start = start;
        this.end = end;
        this.profile = profile;
    }

    public abstract T run(Entity entity);

}
