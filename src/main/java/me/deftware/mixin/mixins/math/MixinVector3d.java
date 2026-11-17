package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.Vector3;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Vec3.class)
public class MixinVector3d implements Vector3<Double> {

    @Unique
    @Override
    public Double getX() {
        return ((Vec3) (Object) this).x();
    }

    @Unique
    @Override
    public Double getY() {
        return ((Vec3) (Object) this).y();
    }

    @Unique
    @Override
    public Double getZ() {
        return ((Vec3) (Object) this).z();
    }

    @Unique
    @Override
    public Vector3<Double> add(Double x, Double y, Double z) {
        return (Vector3<Double>) ((Vec3) (Object) this).add(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Double> subtract(Double x, Double y, Double z) {
        return (Vector3<Double>) ((Vec3) (Object) this).subtract(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Double> multiply(Double x, Double y, Double z) {
        return (Vector3<Double>) ((Vec3) (Object) this).multiply(x, y, z);
    }

    @Unique
    @Override
    public double distanceTo(Double x, Double y, Double z) {
        return Math.sqrt(((Vec3) (Object) this).distanceToSqr(x, y, z));
    }

}
