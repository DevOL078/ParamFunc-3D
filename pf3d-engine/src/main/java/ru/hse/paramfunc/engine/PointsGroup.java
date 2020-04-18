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
        List<FunctionPoint> points = FunctionValueStorage.getInstance().getAllPoints();
        normalizePoints(points);
        this.spacePoints = points.stream()
                .map(SpacePoint::new)
                .collect(Collectors.toList());
        List<Sphere> spheres = this.spacePoints.stream()
                .map(SpacePoint::getSphere)
                .collect(Collectors.toList());
        super.getChildren().addAll(spheres);
    }

    private void normalizePoints(List<FunctionPoint> points) {
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
    }

}
