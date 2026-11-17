package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.util.minecraft.EntitySwingResult;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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

        net.minecraft.world.entity.Entity entity = in.getMinecraftEntity();

        HitResult hitResult = raycast(start, end, entity);

        double distance = maxDistance * maxDistance;
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK)
            distance = hitResult.getLocation().distanceToSqr((Vec3) start);

        AABB box = entity.getBoundingBox().expandTowards((Vec3) rotation.multiply(maxDistance)).inflate(1.0D, 1.0D, 1.0D);

        EntityHitResult result = ProjectileUtil.getEntityHitResult(entity, (Vec3) start, (Vec3) end, box, e -> (!e.isSpectator() && e.isPickable()), distance);

        if (result != null) {
            double g = start.distanceTo((Vector3<Double>) result.getLocation());
            if (g < distance) {
                return new EntitySwingResult(result);
            }
        }

        return null;
    }

    public HitResult raycast(Vector3<Double> start, Vector3<Double> end, net.minecraft.world.entity.Entity entity) {
        return Minecraft.getInstance().level.clip(new ClipContext((Vec3) start, (Vec3) end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
    }

}
