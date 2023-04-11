package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.Vector3;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Vec3d.class)
public class MixinVector3d implements Vector3<Double> {

    @Unique
    @Override
    public Double getX() {
        return ((Vec3d) (Object) this).getX();
    }

    @Unique
    @Override
    public Double getY() {
        return ((Vec3d) (Object) this).getY();
    }

    @Unique
    @Override
    public Double getZ() {
        return ((Vec3d) (Object) this).getZ();
    }

    @Unique
    @Override
    public Vector3<Double> add(Double x, Double y, Double z) {
        return (Vector3<Double>) ((Vec3d) (Object) this).add(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Double> subtract(Double x, Double y, Double z) {
        return (Vector3<Double>) ((Vec3d) (Object) this).subtract(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Double> multiply(Double x, Double y, Double z) {
        return (Vector3<Double>) ((Vec3d) (Object) this).multiply(x, y, z);
    }

    @Unique
    @Override
    public double distanceTo(Double x, Double y, Double z) {
        return Math.sqrt(((Vec3d) (Object) this).squaredDistanceTo(x, y, z));
    }

}
