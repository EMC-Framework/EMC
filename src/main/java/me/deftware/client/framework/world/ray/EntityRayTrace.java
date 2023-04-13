package me.deftware.client.framework.world.ray;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;
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
    private final Vector3<Double> rotation;

    public EntityRayTrace(Vector3<Double> start, Vector3<Double> end, Vector3<Double> rotation, double distance, RayProfile profile) {
        super(start, end, profile);
        this.maxDistance = distance;
        this.rotation = rotation;
    }

    @Override
    public EntitySwingResult run(Entity in) {

        net.minecraft.entity.Entity entity = in.getMinecraftEntity();

        RayTraceResult hitResult = raycast(start, end);

        double distance = maxDistance * maxDistance;
        if (hitResult != null && hitResult.type == RayTraceResult.Type.BLOCK)
            distance = hitResult.hitVec.distanceTo((Vec3d) start);

        Vec3d rotationVector = (Vec3d) rotation;

        List<net.minecraft.entity.Entity> list = Minecraft.getInstance().world.getEntitiesInAABBexcluding(
                entity, entity.getBoundingBox().expand(rotationVector.x * maxDistance, rotationVector.y * maxDistance, rotationVector.z * maxDistance).grow(1.0D, 1.0D, 1.0D), EntitySelectors.NOT_SPECTATING.and(net.minecraft.entity.Entity::canBeCollidedWith)
        );

        net.minecraft.entity.Entity pointedEntity = null;

        for (net.minecraft.entity.Entity entity1 : list) {
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(entity1.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept((Vec3d) start, (Vec3d) end);

            if (axisalignedbb.contains((Vec3d) start)) {
                if (distance >= 0.0D) {
                    pointedEntity = entity1;
                    distance = 0.0D;
                }
            } else if (raytraceresult != null) {
                double d3 = ((Vec3d) start).distanceTo(raytraceresult.hitVec);

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

    public RayTraceResult raycast(Vector3<Double> start, Vector3<Double> end) {
        return Minecraft.getInstance().world.rayTraceBlocks((Vec3d) start, (Vec3d) end, RayTraceFluidMode.NEVER, false, false);
    }

}
