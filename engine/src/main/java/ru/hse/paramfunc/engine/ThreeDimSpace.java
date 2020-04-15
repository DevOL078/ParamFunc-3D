package ru.hse.paramfunc.engine;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.primitives.Text3DMesh;
import ru.hse.paramfunc.element.Line3D;
import ru.hse.paramfunc.element.Rectangle3D;
import ru.hse.paramfunc.element.SpacePoint;

public class ThreeDimSpace extends Group {

    private Group surfaceGroup;

    private Rectangle3D oxy;
    private Rectangle3D oyz;
    private Rectangle3D ozx;
    private Rectangle3D oxyD;
    private Rectangle3D oyzD;
    private Rectangle3D ozxD;

    public ThreeDimSpace() {
        //Создание вспомогательных плоскостей OXY, OYZ, OXZ
        this.surfaceGroup = new Group();
        super.getChildren().add(this.surfaceGroup);

        Line3D oX = new Line3D(
                new SpacePoint(0, 0, 0),
                new SpacePoint(20, 0, 0),
                Color.AQUA,
                2);
        Line3D oY = new Line3D(
                new SpacePoint(0, 0, 0),
                new SpacePoint(0, 20, 0),
                Color.AQUA,
                2);
        Line3D oZ = new Line3D(
                new SpacePoint(0, 0, 0),
                new SpacePoint(0, 0, 20),
                Color.AQUA,
                2);
        Text3DMesh textX = addLabel("X", oX, 0, -6, 0, 0);
        Text3DMesh textY = addLabel("Y", oY, 90, -5, 0, 3);
        Text3DMesh textZ = addLabel("Z", oZ, 0, 0, 0, 0);
        super.getChildren().addAll(oX, oY, oZ, textX, textY, textZ);
    }

    public void setUp() {
        oxy = new Rectangle3D(
                new SpacePoint(100, 0, 0),
                new SpacePoint(100, 100, 0),
                new SpacePoint(0, 100, 0),
                new SpacePoint(0, 0, 0),
                new SpacePoint(100, 0, 0));
        oyz = new Rectangle3D(
                new SpacePoint(0, 100, 0),
                new SpacePoint(0, 100, 100),
                new SpacePoint(0, 0, 100),
                new SpacePoint(0, 0, 0),
                new SpacePoint(0, 100, 0));
        Line3D l11 = new Line3D(new SpacePoint(0, 0, 10), new SpacePoint(0, 100, 10));
        Line3D l12 = new Line3D(new SpacePoint(0, 0, 20), new SpacePoint(0, 100, 20));
        Line3D l13 = new Line3D(new SpacePoint(0, 0, 30), new SpacePoint(0, 100, 30));
        Line3D l14 = new Line3D(new SpacePoint(0, 0, 40), new SpacePoint(0, 100, 40));
        Line3D l15 = new Line3D(new SpacePoint(0, 0, 50), new SpacePoint(0, 100, 50));
        Line3D l16 = new Line3D(new SpacePoint(0, 0, 60), new SpacePoint(0, 100, 60));
        Line3D l17 = new Line3D(new SpacePoint(0, 0, 70), new SpacePoint(0, 100, 70));
        Line3D l18 = new Line3D(new SpacePoint(0, 0, 80), new SpacePoint(0, 100, 80));
        Line3D l19 = new Line3D(new SpacePoint(0, 0, 90), new SpacePoint(0, 100, 90));
//        oyz.addLabel("0.1", l11, -90, -7, 1, 8);
//        oyz.addLabel("0.2", l12, -90, -7, 1, 8);
//        oyz.addLabel("0.3", l13, -90, -7, 1, 8);
//        oyz.addLabel("0.4", l14, -90, -7, 1, 8);
//        oyz.addLabel("0.5", l15, -90, -7, 1, 8);
//        oyz.addLabel("0.6", l16, -90, -7, 1, 8);
//        oyz.addLabel("0.7", l17, -90, -7, 1, 8);
//        oyz.addLabel("0.8", l18, -90, -7, 1, 8);
//        oyz.addLabel("0.9", l19, -90, -7, 1, 8);
        oyz.addAllNodes(l11, l12, l13, l14, l15, l16, l17, l18, l19);
        ozx = new Rectangle3D(
                new SpacePoint(100, 0, 0),
                new SpacePoint(100, 0, 100),
                new SpacePoint(0, 0, 100),
                new SpacePoint(0, 0, 0),
                new SpacePoint(100, 0, 0));
        Line3D l21 = new Line3D(new SpacePoint(100, 0, 10), new SpacePoint(0, 0, 10));
        Line3D l22 = new Line3D(new SpacePoint(100, 0, 20), new SpacePoint(0, 0, 20));
        Line3D l23 = new Line3D(new SpacePoint(100, 0, 30), new SpacePoint(0, 0, 30));
        Line3D l24 = new Line3D(new SpacePoint(100, 0, 40), new SpacePoint(0, 0, 40));
        Line3D l25 = new Line3D(new SpacePoint(100, 0, 50), new SpacePoint(0, 0, 50));
        Line3D l26 = new Line3D(new SpacePoint(100, 0, 60), new SpacePoint(0, 0, 60));
        Line3D l27 = new Line3D(new SpacePoint(100, 0, 70), new SpacePoint(0, 0, 70));
        Line3D l28 = new Line3D(new SpacePoint(100, 0, 80), new SpacePoint(0, 0, 80));
        Line3D l29 = new Line3D(new SpacePoint(100, 0, 90), new SpacePoint(0, 0, 90));
        ozx.addAllNodes(l21, l22, l23, l24, l25, l26, l27, l28, l29);
        this.surfaceGroup.getChildren().addAll(oxy, oyz, ozx);

        oxyD = new Rectangle3D(
                new SpacePoint(100, 0, 100),
                new SpacePoint(100, 100, 100),
                new SpacePoint(0, 100, 100),
                new SpacePoint(0, 0, 100),
                new SpacePoint(100, 0, 100));
        oyzD = new Rectangle3D(
                new SpacePoint(100, 100, 0),
                new SpacePoint(100, 100, 100),
                new SpacePoint(100, 0, 100),
                new SpacePoint(100, 0, 0),
                new SpacePoint(100, 100, 0));
        Line3D l11D = new Line3D(new SpacePoint(100, 100, 10), new SpacePoint(100, 0, 10));
        Line3D l12D = new Line3D(new SpacePoint(100, 100, 20), new SpacePoint(100, 0, 20));
        Line3D l13D = new Line3D(new SpacePoint(100, 100, 30), new SpacePoint(100, 0, 30));
        Line3D l14D = new Line3D(new SpacePoint(100, 100, 40), new SpacePoint(100, 0, 40));
        Line3D l15D = new Line3D(new SpacePoint(100, 100, 50), new SpacePoint(100, 0, 50));
        Line3D l16D = new Line3D(new SpacePoint(100, 100, 60), new SpacePoint(100, 0, 60));
        Line3D l17D = new Line3D(new SpacePoint(100, 100, 70), new SpacePoint(100, 0, 70));
        Line3D l18D = new Line3D(new SpacePoint(100, 100, 80), new SpacePoint(100, 0, 80));
        Line3D l19D = new Line3D(new SpacePoint(100, 100, 90), new SpacePoint(100, 0, 90));
//        oyzD.addLabel("0.1", l11D, 90, -7, 1, -8);
//        oyzD.addLabel("0.2", l12D, 90, -7, 1, -8);
//        oyzD.addLabel("0.3", l13D, 90, -7, 1, -8);
//        oyzD.addLabel("0.4", l14D, 90, -7, 1, -8);
//        oyzD.addLabel("0.5", l15D, 90, -7, 1, -8);
//        oyzD.addLabel("0.6", l16D, 90, -7, 1, -8);
//        oyzD.addLabel("0.7", l17D, 90, -7, 1, -8);
//        oyzD.addLabel("0.8", l18D, 90, -7, 1, -8);
//        oyzD.addLabel("0.9", l19D, 90, -7, 1, -8);
        oyzD.addAllNodes(l11D, l12D, l13D, l14D, l15D, l16D, l17D, l18D, l19D);
        ozxD = new Rectangle3D(
                new SpacePoint(100, 100, 0),
                new SpacePoint(100, 100, 100),
                new SpacePoint(0, 100, 100),
                new SpacePoint(0, 100, 0),
                new SpacePoint(100, 100, 0));
        Line3D l21D = new Line3D(new SpacePoint(0, 100, 10), new SpacePoint(100, 100, 10));
        Line3D l22D = new Line3D(new SpacePoint(0, 100, 20), new SpacePoint(100, 100, 20));
        Line3D l23D = new Line3D(new SpacePoint(0, 100, 30), new SpacePoint(100, 100, 30));
        Line3D l24D = new Line3D(new SpacePoint(0, 100, 40), new SpacePoint(100, 100, 40));
        Line3D l25D = new Line3D(new SpacePoint(0, 100, 50), new SpacePoint(100, 100, 50));
        Line3D l26D = new Line3D(new SpacePoint(0, 100, 60), new SpacePoint(100, 100, 60));
        Line3D l27D = new Line3D(new SpacePoint(0, 100, 70), new SpacePoint(100, 100, 70));
        Line3D l28D = new Line3D(new SpacePoint(0, 100, 80), new SpacePoint(100, 100, 80));
        Line3D l29D = new Line3D(new SpacePoint(0, 100, 90), new SpacePoint(100, 100, 90));
        ozxD.addAllNodes(l21D, l22D, l23D, l24D, l25D, l26D, l27D, l28D, l29D);
        oxyD.setVisible(false);
        oyzD.setVisible(false);
        ozxD.setVisible(false);
        this.surfaceGroup.getChildren().addAll(oxyD, oyzD, ozxD);
    }

    public void updateSurfaceVisibility(Bounds bounds) {
        double x = bounds.getCenterX();
        double y = bounds.getCenterY();
        double z = bounds.getCenterZ();

        if (x < 0) {
            oyz.setVisible(false);
            oyzD.setVisible(true);
        } else if (x > 100) {
            oyz.setVisible(true);
            oyzD.setVisible(false);
        }
        if (z > 0) {
            ozx.setVisible(false);
            ozxD.setVisible(true);
        } else if (z < -100) {
            ozx.setVisible(true);
            ozxD.setVisible(false);
        }
        if (y > 0) {
            oxy.setVisible(false);
            oxyD.setVisible(true);
        } else if (y < -100) {
            oxy.setVisible(true);
            oxyD.setVisible(false);
        }
    }

    public float getVisibleOXYCoordinate() {
        return oxy.isVisible() ? 0 : -100;
    }

    public float getVisibleOYZCoordinate() {
        return oyz.isVisible() ? 0 : 100;
    }

    public float getVisibleOZXCoordinate() {
        return ozx.isVisible() ? 0 : -100;
    }

    private Text3DMesh addLabel(String text, Line3D line, double rotate, double deltaX, double deltaY, double deltaZ) {
        Text3DMesh mesh = new Text3DMesh(text);
        mesh.setFontSize(10);
        mesh.setHeight(1);
        mesh.setTextureModeNone(Color.WHITE);

        mesh.setRotationAxis(new javafx.geometry.Point3D(0, 1, 0));
        mesh.setRotate(rotate);
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
