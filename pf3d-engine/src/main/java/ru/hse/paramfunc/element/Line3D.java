package ru.hse.paramfunc.element;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import org.fxyz3d.geometry.Point3D;
import ru.hse.paramfunc.util.AlgebraUtil;

public class Line3D extends Cylinder {

    private static final float DEFAULT_WIDTH = 0.5f;
    private static final Color DEFAULT_COLOR = Color.valueOf("#45A635");

    private final Point3D start;
    private final Point3D end;

    public Line3D(Point3D start, Point3D end) {
        this(start, end, DEFAULT_COLOR, DEFAULT_WIDTH);
    }

    public Line3D(Point3D start, Point3D end, Color color, float width) {
        super.setRadius(width / 2);
        PhongMaterial material = new PhongMaterial(color);
        super.setMaterial(material);
        this.start = start;
        this.end = end;
        setUp();
    }

    private void setUp() {
        //Вычисляем ось поворота между осью цилиндра и прямой (start, end)
        Point3D lineVector = AlgebraUtil.vector(start, end);
        Point3D upPoint = new Point3D(
                0,
                super.getTranslateY() - super.getHeight() / 2,
                0);
        Point3D downPoint = new Point3D(
                0,
                super.getTranslateY() + super.getHeight() / 2,
                0);
        Point3D cylinderAxe = AlgebraUtil.vector(upPoint, downPoint);
        Point3D rotationAxe = AlgebraUtil.vectorMultiply(cylinderAxe, lineVector);
        //Вычисляем угол поворота
        double angle = Math.toDegrees(AlgebraUtil.angleBetweenVectors(cylinderAxe, rotationAxe));
        //Поварачиваем цилиндр
        super.setRotationAxis(new javafx.geometry.Point3D(rotationAxe.x, rotationAxe.y, rotationAxe.z));
        super.setRotate(angle);
        //Вычисляем вектор перемещения
        Point3D vector = AlgebraUtil.middleBetween(start, end);
        //Перемещаем объект на вектор
        super.setTranslateX(vector.x);
        super.setTranslateY(vector.y);
        super.setTranslateZ(vector.z);
        //Масштабируем по размеру отрезка (start, end)
        super.setHeight(AlgebraUtil.vectorLength(lineVector));
    }

    public Point3D getStart() {
        return start;
    }

    public Point3D getEnd() {
        return end;
    }

}
