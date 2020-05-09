package ru.hse.paramfunc.engine;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import org.fxyz3d.shapes.primitives.Text3DMesh;
import ru.hse.paramfunc.coordSystem.CoordinateSystem;
import ru.hse.paramfunc.element.Line3D;
import ru.hse.paramfunc.element.Rectangle3D;
import ru.hse.paramfunc.element.SpacePoint;

public class TwoDimSpace extends CoordinateSystem {

    public TwoDimSpace() {
        super();
        Line3D oX = new Line3D(
                new SpacePoint(0, 0, -0.5),
                new SpacePoint(20, 0, -0.5),
                Color.AQUA,
                2);
        Line3D oY = new Line3D(
                new SpacePoint(0, 0, -0.5),
                new SpacePoint(0, 20, -0.5),
                Color.AQUA,
                2);

        Text3DMesh oxLabel = create3DLabel(
                "X",
                7,
                oX,
                new Point3D(1, 0, 0),
                -90,
                -5,
                0,
                -3);
        Text3DMesh oyLabel = create3DLabel(
                "Y",
                7,
                oY,
                new Point3D(1, 0, 0),
                -90,
                -5,
                0,
                -3);

        super.getChildren().addAll(oX, oY, oxLabel, oyLabel);
    }

    @Override
    public void setUp() {
        Rectangle3D oxy = new Rectangle3D(
                new SpacePoint(100, 0, -0.5),
                new SpacePoint(100, 100, -0.5),
                new SpacePoint(0, 100, -0.5),
                new SpacePoint(0, 0, -0.5),
                new SpacePoint(100, 0, -0.5));
        super.getChildren().add(oxy);
    }

    @Override
    public void update(Bounds bounds) {

    }
}
