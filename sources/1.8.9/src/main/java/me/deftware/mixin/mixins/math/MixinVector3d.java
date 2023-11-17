package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.Vector3;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Vec3.class)
public class MixinVector3d implements Vector3<Double> {

    @Unique
    @Override
    public Double getX() {
        return ((Vec3) (Object) this).xCoord;
    }

    @Unique
    @Override
    public Double getY() {
        return ((Vec3) (Object) this).yCoord;
    }

    @Unique
    @Override
    public Double getZ() {
        return ((Vec3) (Object) this).zCoord;
    }

    @Unique
    @Override
    public Vector3<Double> add(Double x, Double y, Double z) {
        return (Vector3<Double>) ((Vec3) (Object) this).addVector(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Double> subtract(Double x, Double y, Double z) {
        return (Vector3<Double>) ((Vec3) (Object) this).subtract(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Double> multiply(Double x, Double y, Double z) {
        return Vector3.ofDouble(getX() * x, getY() * y, getZ() * z);
    }

    @Unique
    @Override
    public double distanceTo(Double x, Double y, Double z) {
        double d0 = x - getX();
        double d1 = y - getY();
        double d2 = z - getZ();
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

}
