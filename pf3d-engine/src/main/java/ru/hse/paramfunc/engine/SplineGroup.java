package ru.hse.paramfunc.engine;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.FunctionHolder;
import ru.hse.paramfunc.element.SpacePoint;

import java.util.List;
import java.util.stream.Collectors;

public class SplineGroup extends Group {

    private FunctionHolder functionHolder;

    public SplineGroup(FunctionHolder functionHolder) {
        this.functionHolder = functionHolder;
    }

    public void setUp(List<FunctionPoint> splinePoints) {
        super.getChildren().clear();
        List<SpacePoint> splineSpacePoints = splinePoints.stream()
                .map(p -> {
                    SpacePoint spacePoint = new SpacePoint(p);
                    Sphere sphere = new Sphere();
                    PhongMaterial material = new PhongMaterial();
                    material.diffuseColorProperty().bind(this.functionHolder.interpolationColorProperty());
                    sphere.setMaterial(material);
                    sphere.radiusProperty().bind(this.functionHolder.interpolationRadiusProperty());
                    spacePoint.setSphere(sphere);
                    return spacePoint;
                })
                .collect(Collectors.toList());
        super.getChildren().addAll(splineSpacePoints.stream()
                .map(SpacePoint::getSphere)
                .collect(Collectors.toList()));
    }

}
