package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.math.Vector3;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(Box.class)
public class MixinBox implements BoundingBox {

    @Unique
    @Override
    public double getMinX() {
        return ((Box) (Object) this).minX;
    }

    @Unique
    @Override
    public double getMinY() {
        return ((Box) (Object) this).minY;
    }

    @Unique
    @Override
    public double getMinZ() {
        return ((Box) (Object) this).minZ;
    }

    @Unique
    @Override
    public double getMaxX() {
        return ((Box) (Object) this).maxX;
    }

    @Unique
    @Override
    public double getMaxY() {
        return ((Box) (Object) this).maxY;
    }

    @Unique
    @Override
    public double getMaxZ() {
        return ((Box) (Object) this).maxZ;
    }

    @Unique
    @Override
    public Vector3<Double> getCenter() {
        return (Vector3<Double>) ((Box) (Object) this).getCenter();
    }

    @Unique
    @Override
    public BoundingBox offset(double x, double y, double z) {
        return (BoundingBox) ((Box) (Object) this).offset(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Double> rayTrace(Vector3<Double> min, Vector3<Double> max) {
        Optional<Vec3d> result = ((Box) (Object) this).raycast((Vec3d) min, (Vec3d) max);
        return (Vector3<Double>) result.orElse(null);
    }

}
