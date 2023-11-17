package me.deftware.client.framework.math;

import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

/**
 * @author Deftware
 * @param <T> The type of the vector
 */
@SuppressWarnings("unchecked")
public interface Vector3<T extends Number> {

    T getX();

    T getY();

    T getZ();

    Vector3<T> add(T x, T y, T z);

    Vector3<T> subtract(T x, T y, T z);

    Vector3<T> multiply(T x, T y, T z);

    double distanceTo(T x, T y, T z);

    default double getMagnitude() {
        double x = getX().doubleValue();
        double y = getY().doubleValue();
        double z = getZ().doubleValue();
        return Math.sqrt(x * x + y * y + z * z);
    }

    default Vector3<T> multiply(T scalar) {
        return multiply(scalar, scalar, scalar);
    }

    default Vector3<T> add(Vector3<T> vector) {
        return add(vector.getX(), vector.getY(), vector.getZ());
    }

    default Vector3<T> subtract(Vector3<T> vector) {
        return subtract(vector.getX(), vector.getY(), vector.getZ());
    }

    default Vector3<T> multiply(Vector3<T> vector) {
        return multiply(vector.getX(), vector.getY(), vector.getZ());
    }

    default double distanceTo(Vector3<T> vector) {
        return distanceTo(vector.getX(), vector.getY(), vector.getZ());
    }

    static Vector3<Integer> ofInt(int x, int y, int z) {
        return (Vector3<Integer>) new Vec3i(x, y, z);
    }

    static Vector3<Double> ofDouble(double x, double y, double z) {
        return (Vector3<Double>) new Vec3(x, y, z);
    }

}
