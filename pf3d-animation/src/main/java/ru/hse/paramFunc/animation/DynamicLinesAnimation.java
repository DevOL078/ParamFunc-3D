package ru.hse.paramFunc.animation;

import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import ru.hse.paramfunc.domain.Animation;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.domain.FunctionHolder;
import ru.hse.paramfunc.domain.FunctionPoint;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicLinesAnimation extends Animation {

    private final double TRANSITION_DURATION = 3000;
    private final int LINE_POINTS_COUNT = 3;

    private Group pointsGroup;
    private ParallelTransition animation;

    public DynamicLinesAnimation(String name) {
        super(name);
    }

    @Override
    public void init(FunctionHolder functionHolder) {
        this.pointsGroup = new Group();
        this.animation = new ParallelTransition();

        List<FunctionPoint> points = functionHolder.getFunction().getSelectedPoints().stream()
                .sorted(Comparator.comparing(FunctionPoint::getT))
                .collect(Collectors.toList());

        PhongMaterial material = new PhongMaterial();
        material.diffuseColorProperty().bind(functionHolder.animationColorProperty());
        for(int i = 0; i < points.size() - 1; ++i) {
            FunctionPoint startPoint = points.get(i);
            FunctionPoint endPoint = points.get(i + 1);

            ParallelTransition parallelTransition = new ParallelTransition();
            for(int j = 0; j < LINE_POINTS_COUNT; ++j) {
                Sphere sphere = new Sphere();
                sphere.setMaterial(material);
                sphere.radiusProperty().bind(functionHolder.animationRadiusProperty());
                TranslateTransition pointAnimation = new TranslateTransition();
                pointAnimation.durationProperty().bind(functionHolder.animationTimeProperty());
                pointAnimation.setFromX(startPoint.getSystemX());
                pointAnimation.setFromY(-startPoint.getSystemZ());
                pointAnimation.setFromZ(startPoint.getSystemY());
                pointAnimation.setToX(endPoint.getSystemX());
                pointAnimation.setToY(-endPoint.getSystemZ());
                pointAnimation.setToZ(endPoint.getSystemY());
                pointAnimation.setNode(sphere);
                pointAnimation.setDelay(
                        Duration.millis(functionHolder.animationTimeProperty().get().toMillis() / LINE_POINTS_COUNT * j));
                pointAnimation.setCycleCount(Timeline.INDEFINITE);
                parallelTransition.getChildren().add(pointAnimation);
                this.pointsGroup.getChildren().add(sphere);
            }
            functionHolder.animationTimeProperty().addListener((observableValue, duration, t1) -> {
                for(int j = 0; j < parallelTransition.getChildren().size(); ++j) {
                    javafx.animation.Animation animation = parallelTransition.getChildren().get(j);
                    animation.setDelay(
                            Duration.millis(functionHolder.animationTimeProperty().get().toMillis() / LINE_POINTS_COUNT * j));
                }
            });
            animation.getChildren().add(parallelTransition);
        }
        functionHolder.animationTimeProperty().addListener((observableValue, duration, t1) -> {
            animation.stop();
            animation.play();
        });
    }

    @Override
    public void start() {
        if(super.group != null && super.group.getChildren().isEmpty() && this.pointsGroup != null) {
            super.group.getChildren().add(this.pointsGroup);
        }
        if(this.animation != null) {
            this.animation.play();
            this.isRunning = true;
        }
    }

    @Override
    public void pause() {
        if(this.animation != null) {
            this.animation.pause();
            this.isRunning = false;
        }
    }

    @Override
    public void stop() {
        if(super.group != null && this.pointsGroup != null) {
            super.group.getChildren().remove(this.pointsGroup);
        }
        if(this.animation != null) {
            this.animation.stop();
            this.isRunning = false;
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
            this.isRunning = false;
            this.animation = null;
        }
    }

    @Override
    public Animation copy() {
        return new DynamicLinesAnimation(this.getName());
    }

}
