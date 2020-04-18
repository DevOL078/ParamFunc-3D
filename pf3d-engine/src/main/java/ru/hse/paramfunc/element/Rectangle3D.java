package ru.hse.paramfunc.element;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.primitives.Text3DMesh;

public class Rectangle3D extends Group {

    public Rectangle3D(Point3D... points) {
        for (int i = 0; i < points.length - 1; ++i) {
            Point3D a = points[i];
            Point3D b = points[i + 1];
            Line3D line = new Line3D(a, b);
            super.getChildren().add(line);
        }
    }

    public void addLabel(String text, Line3D line, double rotate, double deltaX, double deltaY, double deltaZ) {
        Text3DMesh mesh = new Text3DMesh(text);
        mesh.setFontSize(10);
        mesh.setHeight(1);
        mesh.setTextureModeNone(Color.valueOf("#45A635"));

        mesh.setRotationAxis(new javafx.geometry.Point3D(0, 1, 0));
        mesh.setRotate(rotate);
        Point3D vector = line.getEnd();
        mesh.setTranslateX(vector.getX() + deltaX);
        mesh.setTranslateY(vector.getY() + deltaY);
        mesh.setTranslateZ(vector.getZ() + deltaZ);

        mesh.setScaleX(0.7);
        mesh.setScaleY(0.7);
        mesh.setScaleZ(0.7);

        super.getChildren().add(mesh);
    }

    public void addNode(Node node) {
        super.getChildren().add(node);
    }

    public void addAllNodes(Node... nodes) {
        super.getChildren().addAll(nodes);
    }

}
