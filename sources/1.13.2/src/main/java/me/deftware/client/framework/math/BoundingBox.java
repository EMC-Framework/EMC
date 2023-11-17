package me.deftware.client.framework.math;

import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author Deftware
 */
public interface BoundingBox {

    double getMinX();

    double getMinY();

    double getMinZ();

    double getMaxX();

    double getMaxY();

    double getMaxZ();

    Vector3<Double> getCenter();

    BoundingBox offset(double x, double y, double z);

    Vector3<Double> rayTrace(Vector3<Double> origin, Vector3<Double> direction);

    static BoundingBox of(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return (BoundingBox) new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

}
