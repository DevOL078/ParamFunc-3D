package ru.hse.paramfunc.element;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import ru.hse.paramFunc.animation.Animation;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.storage.FunctionStorage;
import ru.hse.paramfunc.util.Callback;

public class FunctionHolder {

    private Function function;

    private BooleanProperty isInterpolationShownProperty;
    private ObjectProperty<Animation> currentAnimationProperty;
    private ObjectProperty<Color> valuesColorProperty;
    private DoubleProperty valuesRadiusProperty;
    private ObjectProperty<Color> interpolationColorProperty;
    private DoubleProperty interpolationRadiusProperty;
    private IntegerProperty interpolationPointsNumberProperty;
    private ObjectProperty<Color> animationColorProperty;
    private BooleanProperty focusProperty;

    private Callback startAnimationCallback;
    private Callback pauseAnimationCallback;
    private Callback stopAnimationCallback;

    public FunctionHolder(Function function) {
        this.function = function;
        this.isInterpolationShownProperty = new SimpleBooleanProperty(false);
        this.currentAnimationProperty = new SimpleObjectProperty<>();
        this.valuesColorProperty = new SimpleObjectProperty<>(Color.CHOCOLATE);
        this.valuesRadiusProperty = new SimpleDoubleProperty(2.0);
        this.interpolationColorProperty = new SimpleObjectProperty<>(Color.GOLD);
        this.interpolationRadiusProperty = new SimpleDoubleProperty(0.5);
        this.interpolationPointsNumberProperty = new SimpleIntegerProperty(
                FunctionStorage.getInstance().getDefaultSplinePointsNumber());
        this.interpolationPointsNumberProperty.addListener((observableValue, number, t1) ->
                FunctionStorage.getInstance().updateSpline(this.function, (int)t1));
        this.focusProperty = new SimpleBooleanProperty(false);
    }

    public ObjectProperty<Animation> animationProperty() {
        return this.currentAnimationProperty;
    }

    public BooleanProperty interpolationShownProperty() {
        return this.isInterpolationShownProperty;
    }

    public ObjectProperty<Color> valuesColorProperty() {
        return valuesColorProperty;
    }

    public DoubleProperty valuesRadiusProperty() { return valuesRadiusProperty; }

    public ObjectProperty<Color> interpolationColorProperty() {
        return interpolationColorProperty;
    }

    public DoubleProperty interpolationRadiusProperty() { return interpolationRadiusProperty; }

    public IntegerProperty interpolationPointsNumberProperty() { return interpolationPointsNumberProperty; }

    public ObjectProperty<Color> animationColorProperty() {
        return animationColorProperty;
    }

    public BooleanProperty focusProperty() {
        return focusProperty;
    }

    public Function getFunction() {
        return this.function;
    }

    public void setStartAnimationCallback(Callback startAnimationCallback) {
        this.startAnimationCallback = startAnimationCallback;
    }

    public void setPauseAnimationCallback(Callback pauseAnimationCallback) {
        this.pauseAnimationCallback = pauseAnimationCallback;
    }

    public void setStopAnimationCallback(Callback stopAnimationCallback) {
        this.stopAnimationCallback = stopAnimationCallback;
    }

    public Callback startAnimationCallback() {
        return startAnimationCallback;
    }

    public Callback pauseAnimationCallback() {
        return pauseAnimationCallback;
    }

    public Callback stopAnimationCallback() {
        return stopAnimationCallback;
    }
}
