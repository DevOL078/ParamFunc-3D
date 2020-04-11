package ru.hse.paramfunc.element;

import org.fxyz3d.geometry.Point3D;

public class SpacePoint extends Point3D {

    private double systemX;
    private double systemY;
    private double systemZ;

    public SpacePoint(float x, float y, float z) {
        super(x, -z, -y);
        this.systemX = x;
        this.systemY = -z;
        this.systemZ = -y;
    }

    public SpacePoint(double x, double y, double z) {
        super(x, -z, -y);
        this.systemX = x;
        this.systemY = -z;
        this.systemZ = -y;
    }

    public double getSystemX() {
        return systemX;
    }

    public double getSystemY() {
        return systemY;
    }

    public double getSystemZ() {
        return systemZ;
    }
}
