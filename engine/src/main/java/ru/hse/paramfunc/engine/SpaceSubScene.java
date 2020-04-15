package ru.hse.paramfunc.engine;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Sphere;
import org.fxyz3d.geometry.Point3D;
import ru.hse.paramfunc.element.Line3D;

public class SpaceSubScene extends SubScene {

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    private ThreeDimSpace threeDimSpace;
    private PointsGroup pointsGroup;
    private Group additionalLinesGroup;
    private PerspectiveCamera camera;

    public SpaceSubScene(double v, double v1) {
        super(new Group(), v, v1, true, SceneAntialiasing.DISABLED);
        Group sceneGroup = (Group) super.getRoot();
        this.threeDimSpace = new ThreeDimSpace();
        this.threeDimSpace.setUp();
        this.pointsGroup = new PointsGroup();
        this.pointsGroup.setUp();
        this.additionalLinesGroup = new Group();

        sceneGroup.getChildren().addAll(this.threeDimSpace, this.pointsGroup, this.additionalLinesGroup);
        this.camera = new PerspectiveCamera(true);
    }

    public void setUp() {
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-100);
        super.setCamera(camera);

        super.setFill(Paint.valueOf("#343030"));

        addAdditionalLinesForPoints();

        AnimationTimer frameRateMeter = new AnimationTimer() {

            @Override
            public void handle(long now) {
                long oldFrameTime = frameTimes[frameTimeIndex];
                frameTimes[frameTimeIndex] = now;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
                if (frameTimeIndex == 0) {
                    arrayFilled = true;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                    double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
//                    System.out.println(String.format("Current frame rate: %.3f", frameRate));
                }
            }
        };

        frameRateMeter.start();
    }

    public void onCameraMove(Bounds bounds) {
        this.threeDimSpace.updateSurfaceVisibility(bounds);
    }

    private void addAdditionalLinesForPoints() {
        pointsGroup.getChildren().forEach(node -> {
            Sphere point = (Sphere) node;
            point.setOnMouseEntered(e -> {
                double pointX = point.getTranslateX();
                double pointY = point.getTranslateY();
                double pointZ = point.getTranslateZ();

                float visibleX = this.threeDimSpace.getVisibleOYZCoordinate();
                float visibleY = this.threeDimSpace.getVisibleOXYCoordinate();
                float visibleZ = this.threeDimSpace.getVisibleOZXCoordinate();

                Point3D targetPoint = new Point3D(pointX, pointY, pointZ);
                Point3D line1End = new Point3D(pointX, pointY, visibleZ);
                Point3D line2End = new Point3D(pointX, visibleY, pointZ);
                Point3D line3End = new Point3D(visibleX, pointY, pointZ);

                Line3D line1 = new Line3D(targetPoint, line1End, Color.YELLOW, 0.3f);
                Line3D line2 = new Line3D(targetPoint, line2End, Color.YELLOW, 0.3f);
                Line3D line3 = new Line3D(targetPoint, line3End, Color.YELLOW, 0.3f);
                this.additionalLinesGroup.getChildren().addAll(line1, line2, line3);
            });
            point.setOnMouseExited(e -> {
                this.additionalLinesGroup.getChildren().clear();
            });
        });
    }

}
