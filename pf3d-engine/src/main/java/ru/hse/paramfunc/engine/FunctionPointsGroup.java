package ru.hse.paramfunc.engine;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Sphere;
import ru.hse.paramFunc.animation.Animation;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.element.FunctionHolder;
import ru.hse.paramfunc.element.SpacePoint;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionPointsGroup extends Group {

    private FunctionHolder functionHolder;
    private List<FunctionPoint> valuePoints;
    private List<FunctionPoint> splinePoints;
    private Group valueGroup;
    private SplineGroup splineGroup;
    private Group animationGroup;

    public FunctionPointsGroup(FunctionHolder functionHolder) {
        super();
        this.functionHolder = functionHolder;
        this.valueGroup = new Group();
        this.splineGroup = new SplineGroup();
        this.animationGroup = new Group();
        super.getChildren().addAll(this.valueGroup, this.splineGroup, this.animationGroup);
    }

    public void update() {
//        List<FunctionPoint> points = this.functionHolder.getFunction().getSelectedPoints();
//        List<FunctionPoint> splinePoints = FunctionFileProvider.getSplinePoints(this.functionHolder.getFunction());

        List<SpacePoint> functionPoints = valuePoints.stream()
                .map(SpacePoint::new)
                .collect(Collectors.toList());
        functionPoints.forEach(p -> {
            p.getSphere().addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                SubSceneEngine.getSpaceSubScene().notifyAll(e, p.getFunctionPoint());
            });
            p.getSphere().addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                SubSceneEngine.getSpaceSubScene().notifyAll(e, p.getFunctionPoint());
            });
        });
        List<Sphere> spheres = functionPoints.stream()
                .map(SpacePoint::getSphere)
                .collect(Collectors.toList());

        this.valueGroup.getChildren().clear();
        this.valueGroup.getChildren().addAll(spheres);

        splineGroup.setUp(splinePoints);
        splineGroup.visibleProperty().bind(functionHolder.interpolationShownProperty());

        //При изменении анимации очищаем старую анимацию и инициализируем новую
        this.functionHolder.animationProperty().addListener((observableValue, animation, t1) -> {
            // animation - старое значение
            // t1 - новое значение
            this.animationGroup.getChildren().clear();
            if(animation != null) {
                animation.reset();
            }
            if(t1 != null) {
                t1.init(functionHolder.getFunction());
                this.animationGroup.getChildren().add(t1.getGroup());
            }
        });
        //Настрока колбэков для запуска, паузы и остановки анимаций
        this.functionHolder.setStartAnimationCallback(() -> this.functionHolder.animationProperty().get().start());
        this.functionHolder.setPauseAnimationCallback(() -> this.functionHolder.animationProperty().get().pause());
        this.functionHolder.setStopAnimationCallback(() -> this.functionHolder.animationProperty().get().stop());
    }

    public void destroy() {
        this.valueGroup.getChildren().clear();
        this.splineGroup.getChildren().clear();
    }

    public FunctionHolder getFunctionHolder() {
        return functionHolder;
    }

    public void setValuePoints(List<FunctionPoint> valuePoints) {
        this.valuePoints = valuePoints;
    }

    public void setSplinePoints(List<FunctionPoint> splinePoints) {
        this.splinePoints = splinePoints;
    }

}
