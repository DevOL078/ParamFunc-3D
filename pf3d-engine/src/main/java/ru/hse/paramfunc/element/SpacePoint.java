package ru.hse.paramfunc.element;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import org.fxyz3d.geometry.Point3D;
import ru.hse.paramfunc.domain.FunctionPoint;

public class SpacePoint extends Point3D {

    private final static double DEFAULT_RADIUS = 2;

    private FunctionPoint functionPoint;
    private Sphere sphere;

    public SpacePoint(float x, float y, float z) {
        super(x, -z, y);
        initSphere();
    }

    public SpacePoint(double x, double y, double z) {
        super(x, -z, y);
        initSphere();
    }

    public SpacePoint(FunctionPoint point) {
        super(point.getSystemX(), -point.getSystemZ(), point.getSystemY());
        this.functionPoint = point;
        initSphere();
    }

    public void setSphere(Sphere sphere) {
        this.sphere = sphere;
        this.sphere.setTranslateX(super.x);
        this.sphere.setTranslateY(super.y);
        this.sphere.setTranslateZ(super.z);
    }

    public FunctionPoint getFunctionPoint() {
        return this.functionPoint;
    }

    public Sphere getSphere() {
        return sphere;
    }

    private void initSphere() {
        sphere = new Sphere();
        sphere.setRadius(DEFAULT_RADIUS);
        sphere.setMaterial(new PhongMaterial());
        sphere.setTranslateX(super.x);
        sphere.setTranslateY(super.y);
        sphere.setTranslateZ(super.z);
    }

}
