package me.deftware.client.framework.item;

import me.deftware.client.framework.event.events.EventStructureLocation;
import me.deftware.client.framework.math.Vector3;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.phys.Vec3;

public class ThrowData {
    private static ThrowData firstThrow = null;
    private static ThrowData secondThrow = null;

    private final EyeOfEnder entity;
    public double x;
    public double z;
    public double posX;
    public double posZ;
    private double count = 1;

    public ThrowData(EyeOfEnder entity, double posX, double posZ, double x, double z) {
        this.entity = entity;
        this.posX = posX;
        this.posZ = posZ;
        this.x = x;
        this.z = z;
    }

    public static void resetThrows() {
        if (firstThrow != null && secondThrow != null) {
            firstThrow = null;
            secondThrow = null;
        }
    }

    public static void trackMotion(EyeOfEnder entity, double x, double z) {
        if (firstThrow == null) {
            firstThrow = new ThrowData(entity, entity.getX(), entity.getZ(), x, z);
            return;
        }
        if (firstThrow.sameEntity(entity)) {
            firstThrow.addVec(x, z);
            return;
        }
        if (secondThrow == null) {
            secondThrow = new ThrowData(entity, entity.getX(), entity.getZ(), x, z);
            return;
        }
        if (secondThrow.sameEntity(entity)) {
            secondThrow.addVec(x, z);
        }
    }

    public static void checkIntersection(EyeOfEnder entity) {
        Vec3 vel = entity.getDeltaMovement();
        if (firstThrow != null && secondThrow != null && Math.abs(vel.x * vel.z) <= .0000001 && Math.abs(vel.y) != 0.0D) {
            EventStructureLocation event = new EventStructureLocation(firstThrow.calculateIntersection(secondThrow), EventStructureLocation.StructureType.Stronghold);
            event.broadcast();
        }
    }

    public void addVec(double x, double z) {
        this.x = this.x * count + x / (count + 1);
        this.z = this.z * count + z / (++count);
    }

    /**
     * X = ( - z1*x1p*x2 + x1*z1p*x2 + x1*z2*x2p - x1*x2*z2p ) / (x1*z2 - z1*x2)
     * Z = ( y2/x2 ) * X + y2p - ( y2/x2 )*x2p
     * 1 = d
     * 2 = this
     */
    public Vector3<Double> calculateIntersection(ThrowData d) {
        double x = (-d.z * d.posX * this.x + d.x * d.posZ * this.x + d.x * this.z * this.posX + d.x * -this.x * this.posZ)
                / (d.x * this.z - d.z * this.x);
        double z = (this.z / this.x) * x + this.posZ - (this.z / this.x) * this.posX;

        return Vector3.ofDouble(x, 36, z);
    }

    public boolean sameEntity(EyeOfEnder e) {
        return entity == e;
    }
}
