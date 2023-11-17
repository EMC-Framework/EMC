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
        return ((Box) (Object) this).x1;
    }

    @Unique
    @Override
    public double getMinY() {
        return ((Box) (Object) this).y1;
    }

    @Unique
    @Override
    public double getMinZ() {
        return ((Box) (Object) this).z1;
    }

    @Unique
    @Override
    public double getMaxX() {
        return ((Box) (Object) this).x2;
    }

    @Unique
    @Override
    public double getMaxY() {
        return ((Box) (Object) this).y2;
    }

    @Unique
    @Override
    public double getMaxZ() {
        return ((Box) (Object) this).z2;
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
        Optional<Vec3d> result = ((Box) (Object) this).rayTrace((Vec3d) min, (Vec3d) max);
        return (Vector3<Double>) result.orElse(null);
    }

}
