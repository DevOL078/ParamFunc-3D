package ru.hse.paramfunc.domain;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public abstract class Animation {

    private String name;
    protected Group group;
    protected boolean isRunning;

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

    public boolean isRunning() {
        return isRunning;
    }

    public abstract void init(FunctionHolder functionHolder);
    public abstract void start();
    public abstract void pause();
    public abstract void stop();
    public abstract void reset();
    public abstract Animation copy();

}
