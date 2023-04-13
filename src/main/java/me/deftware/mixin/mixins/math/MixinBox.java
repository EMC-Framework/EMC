package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.math.Vector3;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AxisAlignedBB.class)
public class MixinBox implements BoundingBox {

    @Unique
    @Override
    public double getMinX() {
        return ((AxisAlignedBB) (Object) this).minX;
    }

    @Unique
    @Override
    public double getMinY() {
        return ((AxisAlignedBB) (Object) this).minY;
    }

    @Unique
    @Override
    public double getMinZ() {
        return ((AxisAlignedBB) (Object) this).minZ;
    }

    @Unique
    @Override
    public double getMaxX() {
        return ((AxisAlignedBB) (Object) this).maxX;
    }

    @Unique
    @Override
    public double getMaxY() {
        return ((AxisAlignedBB) (Object) this).maxY;
    }

    @Unique
    @Override
    public double getMaxZ() {
        return ((AxisAlignedBB) (Object) this).maxZ;
    }

    @Unique
    @Override
    public Vector3<Double> getCenter() {
        return (Vector3<Double>) ((AxisAlignedBB) (Object) this).getCenter();
    }

    @Unique
    @Override
    public BoundingBox offset(double x, double y, double z) {
        return (BoundingBox) ((AxisAlignedBB) (Object) this).offset(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Double> rayTrace(Vector3<Double> min, Vector3<Double> max) {
        RayTraceResult result = ((AxisAlignedBB) (Object) this).calculateIntercept((Vec3d) min, (Vec3d) max);
        return result == null ? null : (Vector3<Double>) result.hitVec;
    }

}
