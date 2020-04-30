package ru.hse.paramfunc.engine;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Sphere;
import org.fxyz3d.geometry.Point3D;
import ru.hse.paramFunc.animation.Animation;
import ru.hse.paramFunc.animation.AnimationStorage;
import ru.hse.paramfunc.contract.MouseEventListener;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.element.FunctionHolder;
import ru.hse.paramfunc.element.Line3D;
import ru.hse.paramfunc.listener.Listener;
import ru.hse.paramfunc.storage.FunctionStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceSubScene extends SubScene implements Listener {

    private final static long[] frameTimes = new long[100];
    private static int frameTimeIndex = 0;
    private static boolean arrayFilled = false;
    private static AnimationTimer frameRateMeter;
    private static DoubleProperty fpsValue;

    private ThreeDimSpace threeDimSpace;
    private PointsGroup pointsGroup;
    private Group additionalLinesGroup;
    private Group animationGroup;
    private PerspectiveCamera camera;

    private Map<String, Animation> animationMap;
    private Animation currentAnimation;

    private List<MouseEventListener> listeners = new ArrayList<>();

    // Включается счетчик FPS
    static {
        fpsValue = new SimpleDoubleProperty();
        frameRateMeter = new AnimationTimer() {
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
                    fpsValue.set(frameRate);
                }
            }
        };
        frameRateMeter.start();
    }

    public SpaceSubScene(double v, double v1) {
        super(new Group(), v, v1, true, SceneAntialiasing.DISABLED);
        Group sceneGroup = (Group) super.getRoot();
        this.threeDimSpace = new ThreeDimSpace();
        this.threeDimSpace.setUp();
        this.pointsGroup = new PointsGroup();
        this.additionalLinesGroup = new Group();
        this.animationGroup = new Group();
        this.animationMap = new HashMap<>();

        sceneGroup.getChildren().addAll(
                this.threeDimSpace,
                this.pointsGroup,
                this.additionalLinesGroup,
                this.animationGroup);
        this.camera = new PerspectiveCamera(true);

        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-100);

        super.setCamera(camera);
        super.setFill(Paint.valueOf("#343030"));

        FunctionStorage.getInstance().addListener(this);

        AnimationStorage.getAnimations()
                .forEach(animation -> animationMap.put(animation.getName(), animation));
    }

    public void update() {
        this.pointsGroup.update();
//        this.animationGroup.getChildren().clear();
//
//        this.animationMap.values().forEach(Animation::reset);
//        if (this.currentAnimation != null) {
//            this.currentAnimation.init();
//        }
//        addAdditionalLinesForPoints();
    }

    public void onCameraMove(Bounds bounds) {
        this.threeDimSpace.updateSurfaceVisibility(bounds);
    }

    public void setCurrentAnimation(String name) {
        if (this.currentAnimation != null) {
            this.currentAnimation.reset();
        }
        if (!this.animationGroup.getChildren().isEmpty()) {
            this.animationGroup.getChildren().clear();
        }
        Animation animation = this.animationMap.get(name);
        if (animation == null) {
            throw new IllegalStateException("Animation with name " + name + " was not found");
        }
//        animation.init();
        this.currentAnimation = animation;

        System.out.println("Set current animation: " + name);
    }

    public void startCurrentAnimation() {
        if (this.currentAnimation == null) {
            throw new IllegalStateException("Current animation is null");
        }
        this.currentAnimation.start();
        if (this.animationGroup.getChildren().isEmpty()) {
            this.animationGroup.getChildren().add(this.currentAnimation.getGroup());
        }

        System.out.println("Start animation: " + this.currentAnimation.getName());
    }

    public void pauseCurrentAnimation() {
        if (this.currentAnimation == null) {
            throw new IllegalStateException("Current animation is null");
        }
        this.currentAnimation.pause();
    }

    public void stopCurrentAnimation() {
        if (this.currentAnimation == null) {
            throw new IllegalStateException("Current animation is null");
        }
        this.currentAnimation.stop();
        this.animationGroup.getChildren().remove(this.currentAnimation.getGroup());

        System.out.println("Stop animation: " + this.currentAnimation.getName());
    }

    private void addAdditionalLinesForPoints() {
        pointsGroup.getChildren().forEach(node -> {
            if (!(node instanceof Sphere)) return;
            Sphere point = (Sphere) node;
            point.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
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
            point.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                this.additionalLinesGroup.getChildren().clear();
            });
        });
    }

    public FunctionHolder getFunctionHolderByFunction(Function function) {
        return this.pointsGroup.getFunctionHolderByFunction(function);
    }

    public static DoubleProperty getFpsProperty() {
        return fpsValue;
    }

    public void addMouseEventListener(MouseEventListener listener) {
        this.listeners.add(listener);
    }

    public void notifyAll(MouseEvent event, FunctionPoint target) {
        this.listeners.forEach(l -> l.receive(event, target));
    }

    @Override
    public void receive() {
        update();
    }
}
