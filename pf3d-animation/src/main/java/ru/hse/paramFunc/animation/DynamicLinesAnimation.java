package ru.hse.paramFunc.animation;

import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.storage.FunctionValueStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicLinesAnimation extends Animation {

    private final double TRANSITION_DURATION = 3000;
    private final int LINE_POINTS_COUNT = 3;
    private final double TRANSITION_DELAY = TRANSITION_DURATION / LINE_POINTS_COUNT;

    private Group pointsGroup;
    private ParallelTransition animation;

    public DynamicLinesAnimation(String name) {
        super(name);
    }

    @Override
    public void init() {
        this.pointsGroup = new Group();
        this.animation = new ParallelTransition();

        List<FunctionPoint> points = FunctionValueStorage.getInstance().getSelectedPoints().stream()
                .sorted(Comparator.comparing(FunctionPoint::getT))
                .collect(Collectors.toList());

        for(int i = 0; i < points.size() - 1; ++i) {
            FunctionPoint startPoint = points.get(i);
            FunctionPoint endPoint = points.get(i + 1);

            ParallelTransition parallelTransition = new ParallelTransition();
            for(int j = 0; j < LINE_POINTS_COUNT; ++j) {
                Sphere sphere = createSphere();
                TranslateTransition pointAnimation = new TranslateTransition(Duration.millis(TRANSITION_DURATION));
                pointAnimation.setFromX(startPoint.getSystemX());
                pointAnimation.setFromY(-startPoint.getSystemZ());
                pointAnimation.setFromZ(-startPoint.getSystemY());
                pointAnimation.setToX(endPoint.getSystemX());
                pointAnimation.setToY(-endPoint.getSystemZ());
                pointAnimation.setToZ(-endPoint.getSystemY());
                pointAnimation.setNode(sphere);
                pointAnimation.setDelay(Duration.millis(TRANSITION_DELAY * j));
                pointAnimation.setCycleCount(Timeline.INDEFINITE);
                parallelTransition.getChildren().add(pointAnimation);
                this.pointsGroup.getChildren().add(sphere);
            }
            animation.getChildren().add(parallelTransition);
        }

    }

    @Override
    public void start() {
        if(super.group != null && super.group.getChildren().isEmpty() && this.pointsGroup != null) {
            super.group.getChildren().add(this.pointsGroup);
        }
        if(this.animation != null) {
            this.animation.play();
        }
    }

    @Override
    public void pause() {
        if(this.animation != null) {
            this.animation.pause();
        }
    }

    @Override
    public void stop() {
        if(super.group != null && this.pointsGroup != null) {
            super.group.getChildren().remove(this.pointsGroup);
        }
        if(this.animation != null) {
            this.animation.stop();
        }
    }

    @Override
    public void reset() {
        if(super.group != null) {
            super.group.getChildren().clear();
        }
        if(this.pointsGroup != null) {
            this.pointsGroup.getChildren().clear();
            this.pointsGroup = null;
        }
        if(this.animation != null) {
            this.animation.stop();
            this.animation = null;
        }
    }

    private Sphere createSphere() {
        Sphere sphere = new Sphere();
        sphere.setRadius(0.5);
        sphere.setMaterial(new PhongMaterial(Color.LIGHTCORAL));
        return sphere;
    }

}
