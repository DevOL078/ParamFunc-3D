package ru.hse.paramfunc.coordSystem;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.primitives.Text3DMesh;
import ru.hse.paramfunc.element.Line3D;

public abstract class CoordinateSystem extends Group {

    public abstract void setUp();
    public abstract void update(Bounds bounds);

    protected Text3DMesh create3DLabel(String text,
                                       int fontSize,
                                       Line3D line,
                                       javafx.geometry.Point3D rotateAxe,
                                       double rotateAngle,
                                       double deltaX,
                                       double deltaY,
                                       double deltaZ) {
        Text3DMesh mesh = new Text3DMesh(text);
        mesh.setFontSize(fontSize);
        mesh.setHeight(1);
        mesh.setTextureModeNone(Color.WHITE);

        mesh.setRotationAxis(rotateAxe);
        mesh.setRotate(rotateAngle);
        Point3D vector = line.getEnd();
        mesh.setTranslateX(vector.getX() + deltaX);
        mesh.setTranslateY(vector.getY() + deltaY);
        mesh.setTranslateZ(vector.getZ() + deltaZ);

        mesh.setScaleX(0.7);
        mesh.setScaleY(0.7);
        mesh.setScaleZ(0.7);

        return mesh;
    }

}
