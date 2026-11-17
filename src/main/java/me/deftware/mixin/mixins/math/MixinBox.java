package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.math.Vector3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AABB.class)
public class MixinBox implements BoundingBox {

    @Unique
    @Override
    public double getMinX() {
        return ((AABB) (Object) this).minX;
    }

    @Unique
    @Override
    public double getMinY() {
        return ((AABB) (Object) this).minY;
    }

    @Unique
    @Override
    public double getMinZ() {
        return ((AABB) (Object) this).minZ;
    }

    @Unique
    @Override
    public double getMaxX() {
        return ((AABB) (Object) this).maxX;
    }

    @Unique
    @Override
    public double getMaxY() {
        return ((AABB) (Object) this).maxY;
    }

    @Unique
    @Override
    public double getMaxZ() {
        return ((AABB) (Object) this).maxZ;
    }

    @Unique
    @Override
    public Vector3<Double> getCenter() {
        return (Vector3<Double>) ((AABB) (Object) this).getCenter();
    }

    @Unique
    @Override
    public BoundingBox offset(double x, double y, double z) {
        return (BoundingBox) ((AABB) (Object) this).move(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Double> rayTrace(Vector3<Double> min, Vector3<Double> max) {
        var result = ((AABB) (Object) this).clip((Vec3) min, (Vec3) max);
        return (Vector3<Double>) result.orElse(null);
    }

}
