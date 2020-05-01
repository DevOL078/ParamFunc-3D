package ru.hse.paramfunc.element;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import ru.hse.paramFunc.animation.Animation;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.util.Callback;

public class FunctionHolder {

    private Function function;

    private BooleanProperty isInterpolationShownProperty;
    private ObjectProperty<Animation> currentAnimationProperty;
    private ObjectProperty<Color> valuesColorProperty;
    private ObjectProperty<Color> interpolationColorProperty;
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

    public ObjectProperty<Color> interpolationColorProperty() {
        return interpolationColorProperty;
    }

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
