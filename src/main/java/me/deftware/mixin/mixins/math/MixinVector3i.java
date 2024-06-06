package me.deftware.mixin.mixins.math;

import me.deftware.client.framework.math.Vector3;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Vec3i.class)
public class MixinVector3i implements Vector3<Integer> {

    @Unique
    @Override
    public Integer getX() {
        return ((Vec3i) (Object) this).getX();
    }

    @Unique
    @Override
    public Integer getY() {
        return ((Vec3i) (Object) this).getY();
    }

    @Unique
    @Override
    public Integer getZ() {
        return ((Vec3i) (Object) this).getZ();
    }

    @Unique
    @Override
    public Vector3<Integer> add(Integer x, Integer y, Integer z) {
        return (Vector3<Integer>) ((Vec3i) (Object) this).add(x, y, z);
    }

    @Unique
    @Override
    public Vector3<Integer> subtract(Integer x, Integer y, Integer z) {
        return (Vector3<Integer>) ((Vec3i) (Object) this).add(-x, -y, -z);
    }

    @Unique
    @Override
    public Vector3<Integer> multiply(Integer x, Integer y, Integer z) {
        return Vector3.ofInt(getX() * x, getY() * y, getZ() * z);
    }

    @Unique
    @Override
    public double distanceTo(Integer x, Integer y, Integer z) {
        return Math.sqrt(((Vec3i) (Object) this).getSquaredDistance((double) x, (double) y, (double) z, true));
    }

}
