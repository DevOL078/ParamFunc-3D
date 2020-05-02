package ru.hse.paramfunc.engine;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import ru.hse.paramfunc.SubSceneEngine;
import ru.hse.paramfunc.domain.Animation;
import ru.hse.paramfunc.domain.FunctionPoint;
import ru.hse.paramfunc.domain.FunctionHolder;
import ru.hse.paramfunc.element.SpacePoint;
import ru.hse.paramfunc.storage.FunctionStorage;

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
        this.splineGroup = new SplineGroup(functionHolder);
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

            p.getSphere().radiusProperty().bind(this.functionHolder.valuesRadiusProperty());
            PhongMaterial material = (PhongMaterial) (p.getSphere().getMaterial());
            if(this.functionHolder.focusProperty().get()) {
                material.setDiffuseColor(Color.YELLOW);
            } else {
                material.diffuseColorProperty().bind(this.functionHolder.valuesColorProperty());
            }
        });
        List<Sphere> valuesSpheres = functionPoints.stream()
                .map(SpacePoint::getSphere)
                .collect(Collectors.toList());

        this.valueGroup.getChildren().clear();
        this.valueGroup.getChildren().addAll(valuesSpheres);

        splineGroup.setUp(splinePoints);
        splineGroup.visibleProperty().bind(functionHolder.interpolationShownProperty());

        //При изменении анимации очищаем старую анимацию и инициализируем новую
        this.functionHolder.animationProperty().addListener((observableValue, animation, t1) -> {
            // animation - старое значение
            // t1 - новое значение
            this.animationGroup.getChildren().clear();
            if (animation != null) {
                animation.reset();
            }
            if (t1 != null) {
                t1.init(functionHolder);
                this.animationGroup.getChildren().add(t1.getGroup());
            }
        });
        Animation animation = this.functionHolder.animationProperty().get();
        if(animation != null) {
            if(animation.isRunning()) {
                animation.reset();
                animation.init(this.functionHolder);
                animation.start();
            } else {
                animation.reset();
                animation.init(this.functionHolder);
            }
        }
        //Настрока колбэков для запуска, паузы и остановки анимаций
        this.functionHolder.setStartAnimationCallback(() -> this.functionHolder.animationProperty().get().start());
        this.functionHolder.setPauseAnimationCallback(() -> this.functionHolder.animationProperty().get().pause());
        this.functionHolder.setStopAnimationCallback(() -> this.functionHolder.animationProperty().get().stop());

        this.functionHolder.interpolationPointsNumberProperty().addListener((observableValue, number, t1) ->
                FunctionStorage.getInstance().updateSpline(functionHolder.getFunction(), (int) t1));

        this.functionHolder.focusProperty().unbind();
        this.functionHolder.focusProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                valuesSpheres.forEach(sphere -> {
                    PhongMaterial material = (PhongMaterial) sphere.getMaterial();
                    material.diffuseColorProperty().unbind();
                    material.setDiffuseColor(Color.YELLOW);
                });
            } else {
                valuesSpheres.forEach(sphere -> ((PhongMaterial) sphere.getMaterial())
                        .diffuseColorProperty()
                        .bind(this.functionHolder.valuesColorProperty()));
            }
        });
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

    public Group getValueGroup() {
        return this.valueGroup;
    }

}
