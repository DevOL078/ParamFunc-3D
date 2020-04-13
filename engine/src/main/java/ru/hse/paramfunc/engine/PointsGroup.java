package ru.hse.paramfunc.engine;

import javafx.scene.Group;
import javafx.scene.shape.Sphere;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.element.SpacePoint;
import ru.hse.paramfunc.storage.FunctionValueStorage;

import java.util.List;
import java.util.stream.Collectors;

public class PointsGroup extends Group {

    private List<SpacePoint> spacePoints;

    public void setUp() {
        List<FunctionPoint> points = FunctionValueStorage.getInstance().getPoints();
        this.spacePoints = points.stream()
                .map(SpacePoint::new)
                .collect(Collectors.toList());
        List<Sphere> spheres = this.spacePoints.stream()
                .map(SpacePoint::getSphere)
                .collect(Collectors.toList());
        super.getChildren().addAll(spheres);
    }

}
