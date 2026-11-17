package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.util.hitresult.CrosshairResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

/**
 * @author Deftware
 */
public abstract class RayTrace<T extends CrosshairResult> {

    protected final Vector3<Double> start, end;
    protected final RayProfile profile;

    public RayTrace(Vector3<Double> start, Vector3<Double> end, RayProfile profile) {
        this.start = start;
        this.end = end;
        this.profile = profile;
    }

    protected ClipContext getContext(Entity entity) {
        return new ClipContext(
            (Vec3) start, (Vec3) end, profile.getShape(), profile.getFluidHandling(), entity.getMinecraftEntity()
        );
    }

    public abstract T run(Entity entity);

}
