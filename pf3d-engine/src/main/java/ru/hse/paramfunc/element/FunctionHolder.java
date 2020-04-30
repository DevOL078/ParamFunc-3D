package ru.hse.paramfunc.element;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.hse.paramFunc.animation.Animation;
import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.util.Callback;

public class FunctionHolder {

    private Function function;
    private BooleanProperty isInterpolationShown;
    private ObjectProperty<Animation> currentAnimation;
    private Callback startAnimationCallback;
    private Callback pauseAnimationCallback;
    private Callback stopAnimationCallback;

    public FunctionHolder(Function function) {
        this.function = function;
        this.isInterpolationShown = new SimpleBooleanProperty(false);
        this.currentAnimation = new SimpleObjectProperty<>();
    }

    public BooleanProperty interpolationShownProperty() {
        return this.isInterpolationShown;
    }

    public ObjectProperty<Animation> animationProperty() {
        return this.currentAnimation;
    }

    public Function getFunction() {
        return this.function;
    }

    public void setInterpolationShown(boolean interpolationShown) {
        this.isInterpolationShown.setValue(interpolationShown);
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
