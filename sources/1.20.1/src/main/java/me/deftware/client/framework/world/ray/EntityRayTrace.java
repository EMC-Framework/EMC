package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.util.minecraft.EntitySwingResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

/**
 * @author Deftware
 */
public class EntityRayTrace extends RayTrace<EntitySwingResult> {

    private final double maxDistance;
    private final Vector3<Double> rotation;

    public EntityRayTrace(Vector3<Double> start, Vector3<Double> end, Vector3<Double> rotation, double distance, RayProfile profile) {
        super(start, end, profile);
        this.maxDistance = distance;
        this.rotation = rotation;
    }

    @Override
    public EntitySwingResult run(Entity in) {

        net.minecraft.entity.Entity entity = in.getMinecraftEntity();

        HitResult hitResult = raycast(start, end, entity);

        double distance = maxDistance * maxDistance;
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK)
            distance = hitResult.getPos().squaredDistanceTo((Vec3d) start);

        Box box = entity.getBoundingBox().stretch((Vec3d) rotation.multiply(maxDistance)).expand(1.0D, 1.0D, 1.0D);

        EntityHitResult result = ProjectileUtil.raycast(entity, (Vec3d) start, (Vec3d) end, box, e -> (!e.isSpectator() && e.canHit()), distance);

        if (result != null) {
            double g = start.distanceTo((Vector3<Double>) result.getPos());
            if (g < distance) {
                return new EntitySwingResult(result);
            }
        }

        return null;
    }

    public HitResult raycast(Vector3<Double> start, Vector3<Double> end, net.minecraft.entity.Entity entity) {
        return MinecraftClient.getInstance().world.raycast(new RaycastContext((Vec3d) start, (Vec3d) end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
    }

}
