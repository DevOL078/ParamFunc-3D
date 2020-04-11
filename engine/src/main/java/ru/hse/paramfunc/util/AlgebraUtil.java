package ru.hse.paramfunc.util;

import org.fxyz3d.geometry.Point3D;

public class AlgebraUtil {

    public static Point3D vector(Point3D start, Point3D end) {
        return new Point3D(end.x - start.x, end.y - start.y, end.z - start.z);
    }

    public static double vectorLength(Point3D v) {
        return Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }

    public static double scalarMultiply(Point3D v1, Point3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static double angleBetweenVectors(Point3D v1, Point3D v2) {
        double cos = scalarMultiply(v1, v2) / (vectorLength(v1) * vectorLength(v2));
        return Math.acos(cos);
    }

    public static Point3D middleBetween(Point3D p1, Point3D p2) {
        return new Point3D(
                (p1.x + p2.x) / 2,
                (p1.y + p2.y) / 2,
                (p1.z + p2.z) / 2);
    }

    public static Point3D vectorMultiply(Point3D v1, Point3D v2) {
        float x = v1.y * v2.z - v1.z * v2.y;
        float y = v1.z * v2.x - v1.x * v2.z;
        float z = v1.x * v2.y - v1.y * v2.x;
        return new Point3D(x, y, z);
    }

}
