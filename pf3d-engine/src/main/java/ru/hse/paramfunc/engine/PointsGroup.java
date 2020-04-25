package ru.hse.paramfunc.engine;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Sphere;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.element.SpacePoint;
import ru.hse.paramfunc.storage.FunctionValueStorage;

import java.util.List;
import java.util.stream.Collectors;

public class PointsGroup extends Group {

    private List<SpacePoint> spacePoints;
    private SplineGroup splineGroup;

    public PointsGroup() {
        splineGroup = new SplineGroup();
    }

    public void setUp() {
        List<FunctionPoint> points = FunctionValueStorage.getInstance().getSelectedPoints();
        List<FunctionPoint> splinePoints = FunctionValueStorage.getInstance().getSplinePointsForSelectedPoints();
        normalizePoints(points, splinePoints);

        this.spacePoints = points.stream()
                .map(SpacePoint::new)
                .collect(Collectors.toList());
        this.spacePoints.forEach(p -> {
            p.getSphere().addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                SubSceneEngine.getSpaceSubScene().notifyAll(e, p.getFunctionPoint());
            });
            p.getSphere().addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                SubSceneEngine.getSpaceSubScene().notifyAll(e, p.getFunctionPoint());
            });
        });
        List<Sphere> spheres = this.spacePoints.stream()
                .map(SpacePoint::getSphere)
                .collect(Collectors.toList());
        super.getChildren().clear();
        super.getChildren().addAll(spheres);

        splineGroup.setUp(splinePoints);
        super.getChildren().add(splineGroup);
    }

    public Group getSplineGroup() {
        return this.splineGroup;
    }

    private void normalizePoints(List<FunctionPoint> points, List<FunctionPoint> splinePoints) {
        Float minX = null, maxX = null;
        Float minY = null, maxY = null;
        Float minZ = null, maxZ = null;
        for (FunctionPoint point : points) {
            if (minX == null || point.getOriginalX() < minX) {
                minX = point.getOriginalX();
            }
            if (maxX == null || point.getOriginalX() > maxX) {
                maxX = point.getOriginalX();
            }
            if (minY == null || point.getOriginalY() < minY) {
                minY = point.getOriginalY();
            }
            if (maxY == null || point.getOriginalY() > maxY) {
                maxY = point.getOriginalY();
            }
            if (minZ == null || point.getOriginalZ() < minZ) {
                minZ = point.getOriginalZ();
            }
            if (maxZ == null || point.getOriginalZ() > maxZ) {
                maxZ = point.getOriginalZ();
            }
        }
        for (FunctionPoint point : points) {
            float x = (maxX - minX) != 0 ? (point.getOriginalX() - minX) / (maxX - minX) * 100 : 0;
            float y = (maxY - minY) != 0 ? (point.getOriginalY() - minY) / (maxY - minY) * 100 : 0;
            float z = (maxZ - minZ) != 0 ? (point.getOriginalZ() - minZ) / (maxZ - minZ) * 100 : 0;

            point.setSystemX(x);
            point.setSystemY(y);
            point.setSystemZ(z);
        }
        for (FunctionPoint point : splinePoints) {
            float x = (maxX - minX) != 0 ? (point.getOriginalX() - minX) / (maxX - minX) * 100 : 0;
            float y = (maxY - minY) != 0 ? (point.getOriginalY() - minY) / (maxY - minY) * 100 : 0;
            float z = (maxZ - minZ) != 0 ? (point.getOriginalZ() - minZ) / (maxZ - minZ) * 100 : 0;

            point.setSystemX(x);
            point.setSystemY(y);
            point.setSystemZ(z);
        }
    }

}
