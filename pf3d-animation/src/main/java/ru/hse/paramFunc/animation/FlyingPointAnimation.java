package ru.hse.paramFunc.animation;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.storage.FunctionValueStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FlyingPointAnimation extends Animation {

    private final double TRANSITION_DURATION = 2000;

    private Sphere flyingSphere;
    private SequentialTransition animation;

    public FlyingPointAnimation(String name) {
        super(name);
    }

    @Override
    public void init() {
        List<FunctionPoint> points = FunctionValueStorage.getInstance().getSelectedPoints().stream()
                .sorted(Comparator.comparing(FunctionPoint::getT))
                .collect(Collectors.toList());

        this.flyingSphere = new Sphere();
        flyingSphere.setRadius(1);
        flyingSphere.setMaterial(new PhongMaterial(Color.LIGHTCORAL));

        this.animation = new SequentialTransition();

        for (int i = 0; i < points.size() - 1; ++i) {
            FunctionPoint startPoint = points.get(i);
            FunctionPoint endPoint = points.get(i + 1);

            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(TRANSITION_DURATION));
            translateTransition.setFromX(startPoint.getSystemX());
            translateTransition.setFromY(-startPoint.getSystemZ());
            translateTransition.setFromZ(startPoint.getSystemY());
            translateTransition.setToX(endPoint.getSystemX());
            translateTransition.setToY(-endPoint.getSystemZ());
            translateTransition.setToZ(endPoint.getSystemY());
            translateTransition.setNode(flyingSphere);

            animation.getChildren().add(translateTransition);
        }
    }

    @Override
    public void start() {
        if(super.group != null && super.group.getChildren().isEmpty() && this.flyingSphere != null) {
            super.group.getChildren().add(this.flyingSphere);
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
        if(super.group != null && this.flyingSphere != null) {
            super.group.getChildren().remove(this.flyingSphere);
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
        if(this.flyingSphere != null) {
            this.flyingSphere = null;
        }
        if(this.animation != null) {
            this.animation.stop();
            this.animation = null;
        }
    }

}
