package ru.hse.paramfunc.domain;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ru.hse.paramfunc.settings.AppSettings;
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
    private DoubleProperty animationRadiusProperty;
    private ObjectProperty<Duration> animationTimeProperty;
    private BooleanProperty focusProperty;
    private BooleanProperty visibleProperty;

    private Callback startAnimationCallback;
    private Callback pauseAnimationCallback;
    private Callback stopAnimationCallback;

    public FunctionHolder(Function function) {
        this.function = function;
        this.isInterpolationShownProperty = new SimpleBooleanProperty(false);
        this.currentAnimationProperty = new SimpleObjectProperty<>();
        this.valuesColorProperty = new SimpleObjectProperty<>(AppSettings.functionPointsColorPropertyProperty().get());
        this.valuesRadiusProperty = new SimpleDoubleProperty(AppSettings.functionPointsRadiusPropertyProperty().get());
        this.interpolationColorProperty = new SimpleObjectProperty<>(AppSettings.interpolationPointsColorPropertyProperty().get());
        this.interpolationRadiusProperty = new SimpleDoubleProperty(AppSettings.interpolationPointsRadiusPropertyProperty().get());
        this.interpolationPointsNumberProperty = new SimpleIntegerProperty(AppSettings.interpolationPointsCountPropertyProperty().get());
        this.animationColorProperty = new SimpleObjectProperty<>(AppSettings.animationPointsColorPropertyProperty().get());
        this.animationRadiusProperty = new SimpleDoubleProperty(AppSettings.animationPointsRadiusPropertyProperty().get());
        this.animationTimeProperty = new SimpleObjectProperty<>(javafx.util.Duration.millis(AppSettings.animationSpeedPropertyProperty().get()));
        this.focusProperty = new SimpleBooleanProperty(false);
        this.visibleProperty = new SimpleBooleanProperty(true);
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

    public DoubleProperty valuesRadiusProperty() {
        return valuesRadiusProperty;
    }

    public ObjectProperty<Color> interpolationColorProperty() {
        return interpolationColorProperty;
    }

    public DoubleProperty interpolationRadiusProperty() {
        return interpolationRadiusProperty;
    }

    public IntegerProperty interpolationPointsNumberProperty() {
        return interpolationPointsNumberProperty;
    }

    public ObjectProperty<Color> animationColorProperty() {
        return animationColorProperty;
    }

    public DoubleProperty animationRadiusProperty() {
        return animationRadiusProperty;
    }

    public ObjectProperty<Duration> animationTimeProperty() {
        return animationTimeProperty;
    }

    public BooleanProperty focusProperty() {
        return focusProperty;
    }

    public BooleanProperty visibleProperty() {
        return visibleProperty;
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
