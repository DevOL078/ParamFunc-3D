package ru.hse.paramfunc.engine;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import ru.hse.paramFunc.interpolation.spline.CatmullRomSpline;
import ru.hse.paramFunc.interpolation.spline.Spline;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.element.SpacePoint;
import ru.hse.paramfunc.storage.FunctionValueStorage;

import java.util.List;
import java.util.stream.Collectors;

public class SplineGroup extends Group {

    private final static Spline spline;
    private final static Material pointMaterial = new PhongMaterial(Color.GOLD);
    private final static float pointRadius = 0.5f;

    static {
        spline = new CatmullRomSpline();
    }

    public void setUp(List<FunctionPoint> splinePoints) {
        super.getChildren().clear();
        List<SpacePoint> splineSpacePoints = splinePoints.stream()
                .map(p -> {
                    SpacePoint spacePoint = new SpacePoint(p);
                    Sphere sphere = new Sphere();
                    sphere.setRadius(pointRadius);
                    sphere.setMaterial(pointMaterial);
                    spacePoint.setSphere(sphere);
                    return spacePoint;
                })
                .collect(Collectors.toList());
        super.getChildren().addAll(splineSpacePoints.stream()
                .map(SpacePoint::getSphere)
                .collect(Collectors.toList()));
    }

}
