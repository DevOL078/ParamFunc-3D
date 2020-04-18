package ru.hse.paramfunc.util;

import org.fxyz3d.geometry.Point3D;

public class Quaternion {

    private double s;
    private double x;
    private double y;
    private double z;
    private Point3D vector;
    private double angle;

    public Quaternion(double s, double x, double y, double z) {
        this.s = s;
        this.x = x;
        this.y = y;
        this.z = z;

        angle = 2 * Math.acos(s) / Math.PI * 180;
        double vX = x / Math.sqrt(1 - s * s);
        double vY = y / Math.sqrt(1 - s * s);
        double vZ = z / Math.sqrt(1 - s * s);
        vector = new Point3D(vX, vY, vZ);
    }

    public Quaternion(Point3D vector, double angle) {
        this.angle = angle;
        this.vector = vector;
        angle = Math.PI / 180 * angle;
        s = Math.cos(angle / 2);
        x = Math.sin(angle / 2) * vector.x;
        y = Math.sin(angle / 2) * vector.y;
        z = Math.sin(angle / 2) * vector.z;
    }

    public double S() {
        return s;
    }

    public double X() {
        return x;
    }

    public double Y() {
        return y;
    }

    public double Z() {
        return z;
    }

    public Point3D getVector() {
        return vector;
    }

    public double getAngle() {
        return angle;
    }

    public Quaternion sum(Quaternion other) {
        Quaternion sum = new Quaternion(s + other.S(), x + other.X(),
                y + other.Y(), z + other.Z());
        return sum;
    }

    public Quaternion multiply(Quaternion other) {
        double newS = s * other.S() - x * other.X() - y * other.Y() - z * other.Z();
        double newX = s * other.X() + x * other.S() + y * other.Z() - z * other.Y();
        double newY = s * other.Y() - x * other.Z() + y * other.S() + z * other.X();
        double newZ = s * other.Z() + x * other.Y() - y * other.X() + z * other.S();
        return new Quaternion(newS, newX, newY, newZ);
    }

    //длина
    public double length() {
        return Math.sqrt(s * s + x * x + y * y + z * z);
    }

    //обратный кватернион
    public Quaternion inverse() {
        return new Quaternion(s, -x, -y, -z);
    }

    //деление на число
    public Quaternion multiplyScalar(double a) {
        return new Quaternion(s * a, x * a, y * a, z * a);
    }

    //нормирование
    public Quaternion normalize() {
        return new Quaternion(s / length(), x / length(), y / length(), z / length());
    }

    public static double dotProduct(Quaternion q1, Quaternion q2) {
        return q1.S() * q2.S() + q1.X() * q2.X() + q1.Y() * q2.Y() + q1.Z() * q2.Z();
    }

    //интерполированный кватернион
    public static Quaternion interQuaternion(Quaternion start, Quaternion finish, double t) {
        double cosAngle = dotProduct(start, finish) / start.length() / finish.length();
        double angle = Math.acos(cosAngle);
        double coeff1 = Math.sin((1 - t) * angle) / Math.sin(angle);
        double coeff2 = Math.sin(t * angle) / Math.sin(angle);
        Quaternion result = start.multiplyScalar(coeff1).sum(finish.multiplyScalar(coeff2));
        return result;
    }

}
