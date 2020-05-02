package ru.hse.paramFunc.animation;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import ru.hse.paramfunc.domain.Function;

public abstract class Animation {

    private String name;
    protected Group group;

    public Animation(String name) {
        this.name = name;
        this.group = new Group();
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    public abstract void init(Function function, Color sphereColor, double sphereRadius, int animationTime);
    public abstract void start();
    public abstract void pause();
    public abstract void stop();
    public abstract void reset();
    public abstract Animation copy();

}
