package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.vector.Vector3d;
import me.deftware.client.framework.util.minecraft.EntitySwingResult;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceFluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * @author Deftware
 */
public class EntityRayTrace extends RayTrace<EntitySwingResult> {

    private final double maxDistance;
    private final Vector3d rotation;

    public EntityRayTrace(Vector3d start, Vector3d end, Vector3d rotation, double distance, RayProfile profile) {
        super(start, end, profile);
        this.maxDistance = distance;
        this.rotation = rotation;
    }

    @Override
    public EntitySwingResult run(Entity in) {

        net.minecraft.entity.Entity entity = in.getMinecraftEntity();

        RayTraceResult hitResult = entity.rayTrace(maxDistance, 1f, RayTraceFluidMode.NEVER);

        double distance = maxDistance * maxDistance;
        if (hitResult != null && hitResult.type == RayTraceResult.Type.BLOCK)
            distance = hitResult.hitVec.distanceTo(start.getMinecraftVector());

        Vec3d rotationVector = rotation.getMinecraftVector();

        List<net.minecraft.entity.Entity> list = Minecraft.getInstance().world.getEntitiesInAABBexcluding(
                entity, entity.getBoundingBox().expand(rotationVector.x * maxDistance, rotationVector.y * maxDistance, rotationVector.z * maxDistance).grow(1.0D, 1.0D, 1.0D), EntitySelectors.NOT_SPECTATING.and(net.minecraft.entity.Entity::canBeCollidedWith)
        );

        net.minecraft.entity.Entity pointedEntity = null;

        for (net.minecraft.entity.Entity entity1 : list) {
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(entity1.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start.getMinecraftVector(), end.getMinecraftVector());

            if (axisalignedbb.contains(start.getMinecraftVector())) {
                if (distance >= 0.0D) {
                    pointedEntity = entity1;
                    distance = 0.0D;
                }
            } else if (raytraceresult != null) {
                double d3 = start.getMinecraftVector().distanceTo(raytraceresult.hitVec);

                if (d3 < distance || distance == 0.0D) {
                    if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
                        if (distance == 0.0D) {
                            pointedEntity = entity1;
                        }
                    } else {
                        pointedEntity = entity1;
                        distance = d3;
                    }
                }
            }

        }

        if (pointedEntity != null) {
            return new EntitySwingResult(pointedEntity);
        }

        return null;
    }

}
