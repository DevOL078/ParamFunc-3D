package ru.hse.paramfunc.engine;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import org.fxyz3d.geometry.Point3D;
import ru.hse.paramFunc.animation.AnimationStorage;
import ru.hse.paramfunc.coordSystem.CoordinateSystem;
import ru.hse.paramfunc.domain.Animation;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionHolder;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.element.Line3D;
import ru.hse.paramfunc.element.SpacePoint;
import ru.hse.paramfunc.event.EventListener;
import ru.hse.paramfunc.event.EventMediator;
import ru.hse.paramfunc.event.EventType;
import ru.hse.paramfunc.storage.FunctionStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpaceSubScene extends SubScene implements EventListener {

    private final static long[] frameTimes = new long[100];
    private static int frameTimeIndex = 0;
    private static boolean arrayFilled = false;
    private static AnimationTimer frameRateMeter;
    private static DoubleProperty fpsValue;

    private Group rootGroup;
    private CoordinateSystem coordinateSystem;
    private PointsGroup pointsGroup;
    private Group additionalLinesGroup;
    private Group animationGroup;
    private boolean is3DCoordinateSystem;

    private Map<String, Animation> animationMap;

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
        this.rootGroup = (Group) super.getRoot();
        this.pointsGroup = new PointsGroup();
        this.additionalLinesGroup = new Group();
        this.animationGroup = new Group();
        this.animationMap = new HashMap<>();

        this.rootGroup.getChildren().addAll(
                this.pointsGroup,
                this.additionalLinesGroup,
                this.animationGroup);
        super.setFill(Paint.valueOf("#343030"));

        EventMediator.addListener(EventType.FUNCTION_LIST_UPDATE, this);

        AnimationStorage.getAnimations()
                .forEach(animation -> animationMap.put(animation.getName(), animation));
    }

    public void updateCoordinateSystem() {
        List<FunctionPoint> allPoints = FunctionStorage.getInstance().getFunctions().stream()
                .map(Function::getAllPoints)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (allPoints.isEmpty()) {
            if (this.coordinateSystem != null) {
                this.rootGroup.getChildren().remove(this.coordinateSystem);
                this.coordinateSystem = null;
            }
            return;
        }
        boolean isAllZEqualsZero = allPoints.stream()
                .map(p -> p.getOriginalZ() == 0)
                .reduce(true, (a, b) -> a && b);
        if (isAllZEqualsZero && (is3DCoordinateSystem || this.coordinateSystem == null)) {
            if (this.coordinateSystem != null) {
                this.rootGroup.getChildren().remove(this.coordinateSystem);
            }
            this.coordinateSystem = new TwoDimSpace();
            this.coordinateSystem.setUp();
            this.rootGroup.getChildren().add(this.coordinateSystem);
            this.is3DCoordinateSystem = false;
            updateCamera();
        } else if (!isAllZEqualsZero && (!is3DCoordinateSystem || this.coordinateSystem == null)) {
            if (this.coordinateSystem != null) {
                this.rootGroup.getChildren().remove(this.coordinateSystem);
            }
            this.coordinateSystem = new ThreeDimSpace();
            this.coordinateSystem.setUp();
            this.rootGroup.getChildren().add(this.coordinateSystem);
            this.is3DCoordinateSystem = true;
            updateCamera();
        }
    }

    public void updateCamera() {
        if (this.is3DCoordinateSystem) {
            CameraController.setUpForThreeDimSpace();
        } else {
            CameraController.setUpForTwoDimSpace();
        }
    }

    public void updatePointsGroup() {
        this.pointsGroup.update();
        addAdditionalLinesForPoints();
    }

    public void onCameraMove(Bounds bounds) {
        this.coordinateSystem.update(bounds);
    }

    private void addAdditionalLinesForPoints() {
        pointsGroup.getChildren().forEach(node -> {
            if (!(node instanceof FunctionPointsGroup)) return;
            FunctionPointsGroup functionPointsGroup = (FunctionPointsGroup) node;
            Group valueGroup = functionPointsGroup.getValueGroup();
            valueGroup.getChildren().forEach(valueNode -> {
                if (!(valueNode instanceof Sphere)) return;
                Sphere point = (Sphere) valueNode;
                point.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                    if (this.is3DCoordinateSystem) {
                        double pointX = point.getTranslateX();
                        double pointY = point.getTranslateY();
                        double pointZ = point.getTranslateZ();

                        ThreeDimSpace threeDimSpace = (ThreeDimSpace) this.coordinateSystem;
                        float visibleX = threeDimSpace.getVisibleOYZCoordinate();
                        float visibleY = threeDimSpace.getVisibleOXYCoordinate();
                        float visibleZ = threeDimSpace.getVisibleOZXCoordinate();

                        Point3D targetPoint = new Point3D(pointX, pointY, pointZ);
                        Point3D line1End = new Point3D(pointX, pointY, visibleZ);
                        Point3D line2End = new Point3D(pointX, visibleY, pointZ);
                        Point3D line3End = new Point3D(visibleX, pointY, pointZ);

                        Line3D line1 = new Line3D(targetPoint, line1End, Color.YELLOW, 0.3f);
                        Line3D line2 = new Line3D(targetPoint, line2End, Color.YELLOW, 0.3f);
                        Line3D line3 = new Line3D(targetPoint, line3End, Color.YELLOW, 0.3f);
                        this.additionalLinesGroup.getChildren().addAll(line1, line2, line3);
                    } else {
                        double pointX = point.getTranslateX();
                        double pointZ = point.getTranslateZ();

                        Point3D targetPoint = new Point3D(pointX, 0, pointZ);
                        Point3D line1End = new Point3D(0, 0, pointZ);
                        Point3D line2End = new Point3D(pointX, 0, 0);

                        Line3D line1 = new Line3D(targetPoint, line1End, Color.YELLOW, 0.5f);
                        Line3D line2 = new Line3D(targetPoint, line2End, Color.YELLOW, 0.5f);
                        this.additionalLinesGroup.getChildren().addAll(line1, line2);
                    }
                });
                point.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                    this.additionalLinesGroup.getChildren().clear();
                });
            });
        });
    }

    public FunctionHolder getFunctionHolderByFunction(Function function) {
        return this.pointsGroup.getFunctionHolderByFunction(function);
    }

    public List<FunctionHolder> getAllFunctionHolders() {
        return this.pointsGroup.getAllFunctionHolders();
    }

    public static DoubleProperty getFpsProperty() {
        return fpsValue;
    }

    public void findPoints(Integer t, Double x, Double y, Double z) {
        pointsGroup.getChildren().forEach(node -> {
            if (!(node instanceof FunctionPointsGroup)) return;
            FunctionPointsGroup functionPointsGroup = (FunctionPointsGroup) node;
            List<FunctionPoint> searchedPoints = functionPointsGroup.getValuePoints().stream()
                    .filter(p -> {
                        boolean result = true;
                        if (t != null) {
                            result = t == p.getT();
                        }
                        if (x != null) {
                            result = result && x == p.getOriginalX();
                        }
                        if (y != null) {
                            result = result && y == p.getOriginalY();
                        }
                        if (z != null) {
                            result = result && z == p.getOriginalZ();
                        }
                        return result;
                    })
                    .collect(Collectors.toList());
            searchedPoints.forEach(p -> {
                SpacePoint spacePoint = functionPointsGroup.getSpacePointByFunctionPoint(p);
                Sphere sphere = spacePoint.getSphere();
                SequentialTransition sequentialTransition = new SequentialTransition();
                ScaleTransition increaseScaleTransition = new ScaleTransition(Duration.millis(500));
                increaseScaleTransition.setFromX(sphere.getScaleX());
                increaseScaleTransition.setFromY(sphere.getScaleY());
                increaseScaleTransition.setFromZ(sphere.getScaleZ());
                increaseScaleTransition.setToX(sphere.getScaleX() * 2);
                increaseScaleTransition.setToY(sphere.getScaleY() * 2);
                increaseScaleTransition.setToZ(sphere.getScaleZ() * 2);
                increaseScaleTransition.setNode(sphere);
                ScaleTransition decreaseScaleTransition = new ScaleTransition(Duration.millis(500));
                decreaseScaleTransition.setFromX(sphere.getScaleX() * 2);
                decreaseScaleTransition.setFromY(sphere.getScaleY() * 2);
                decreaseScaleTransition.setFromZ(sphere.getScaleZ() * 2);
                decreaseScaleTransition.setToX(sphere.getScaleX());
                decreaseScaleTransition.setToY(sphere.getScaleY());
                decreaseScaleTransition.setToZ(sphere.getScaleZ());
                decreaseScaleTransition.setNode(sphere);
                sequentialTransition.getChildren().addAll(increaseScaleTransition, decreaseScaleTransition);
                sequentialTransition.setCycleCount(5);
                sequentialTransition.play();
            });
        });
    }

    @Override
    public void receive(EventType eventType, Object... args) {
        if (eventType == EventType.FUNCTION_LIST_UPDATE) {
            updateCoordinateSystem();
            updatePointsGroup();
        }
    }
}
