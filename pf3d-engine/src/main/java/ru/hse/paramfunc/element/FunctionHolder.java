package ru.hse.paramfunc.element;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import ru.hse.paramFunc.animation.Animation;
import ru.hse.paramfunc.domain.Function;

public class FunctionHolder {

    private Function function;
    private BooleanProperty isInterpolationShown;
    private ObjectProperty<Animation> currentAnimation;

    public FunctionHolder(Function function) {
        this.function = function;
        this.isInterpolationShown = new SimpleBooleanProperty(false);
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

}
