package ru.hse.paramfunc.engine;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Paint;

public class SpaceSubScene extends SubScene {

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    private ThreeDimSpace threeDimSpace;
    private PointsGroup pointsGroup;
    private PerspectiveCamera camera;

    public SpaceSubScene(double v, double v1) {
        super(new Group(), v, v1, true, SceneAntialiasing.DISABLED);
        Group sceneGroup = (Group) super.getRoot();
        this.threeDimSpace = new ThreeDimSpace();
        this.threeDimSpace.setUp();
        this.pointsGroup = new PointsGroup();
        this.pointsGroup.setUp();

        sceneGroup.getChildren().addAll(this.threeDimSpace, this.pointsGroup);
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

}
