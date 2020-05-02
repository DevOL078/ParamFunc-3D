package ru.hse.paramfunc.domain;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
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

    private Callback startAnimationCallback;
    private Callback pauseAnimationCallback;
    private Callback stopAnimationCallback;

    private boolean isAnimationStarted;

    public FunctionHolder(Function function) {
        this.function = function;
        this.isInterpolationShownProperty = new SimpleBooleanProperty(false);
        this.currentAnimationProperty = new SimpleObjectProperty<>();
        this.valuesColorProperty = new SimpleObjectProperty<>(Color.CHOCOLATE);
        this.valuesRadiusProperty = new SimpleDoubleProperty(2.0);
        this.interpolationColorProperty = new SimpleObjectProperty<>(Color.GOLD);
        this.interpolationRadiusProperty = new SimpleDoubleProperty(0.5);
        this.interpolationPointsNumberProperty = new SimpleIntegerProperty(10);
        this.animationColorProperty = new SimpleObjectProperty<>(Color.LIGHTCORAL);
        this.animationRadiusProperty = new SimpleDoubleProperty(1.0);
        this.animationTimeProperty = new SimpleObjectProperty<>(javafx.util.Duration.millis(2000));
//        this.animationColorProperty.addListener((observableValue, color, t1) -> {
//            initAnimation(this.currentAnimationProperty.get());
//            if (this.isAnimationStarted) {
//                this.currentAnimationProperty.get().start();
//            }
//        });
//        this.animationRadiusProperty.addListener((observableValue, color, t1) -> {
//            initAnimation(this.currentAnimationProperty.get());
//            if (this.isAnimationStarted) {
//                this.currentAnimationProperty.get().start();
//            }
//        });
//        this.animationTimeProperty.addListener((observableValue, color, t1) -> {
//            initAnimation(this.currentAnimationProperty.get());
//            if (this.isAnimationStarted) {
//                this.currentAnimationProperty.get().start();
//            }
//        });
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
        return () -> {
            this.isAnimationStarted = true;
            startAnimationCallback.call();
        };
    }

    public Callback pauseAnimationCallback() {
        return pauseAnimationCallback;
    }

    public Callback stopAnimationCallback() {
        return () -> {
            this.isAnimationStarted = false;
            stopAnimationCallback.call();
        };
    }

//    private void initAnimation(Animation animation) {
//        if (animation != null) {
//            animation.reset();
//            animation.init(
//                    function,
//                    animationColorProperty.get(),
//                    animationRadiusProperty.get(),
//                    animationTimeProperty.get());
//        }
//    }
}
