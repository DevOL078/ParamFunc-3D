package ru.hse.paramfunc.element;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import org.fxyz3d.geometry.Point3D;
import ru.hse.paramfunc.domain.FunctionPoint;

public class SpacePoint extends Point3D {

    private final static double DEFAULT_RADIUS = 2;
    private final static Material DEFAULT_MATERIAL = new PhongMaterial(Color.CHOCOLATE);

    private Sphere sphere;

    public SpacePoint(float x, float y, float z) {
        super(x, -z, -y);
        initSphere();
    }

    public SpacePoint(double x, double y, double z) {
        super(x, -z, -y);
        initSphere();
    }

    public SpacePoint(FunctionPoint point) {
        super(point.getSystemX(), -point.getSystemZ(), -point.getSystemY());
        initSphere();
    }

    public Sphere getSphere() {
        return sphere;
    }

    private void initSphere() {
        sphere = new Sphere();
        sphere.setRadius(DEFAULT_RADIUS);
        sphere.setMaterial(DEFAULT_MATERIAL);
        sphere.setTranslateX(super.x);
        sphere.setTranslateY(super.y);
        sphere.setTranslateZ(super.z);
    }

}
