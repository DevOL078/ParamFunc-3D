package ru.hse.paramFunc.animation;

import javafx.scene.Group;

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

    public abstract void init();
    public abstract void start();
    public abstract void pause();
    public abstract void stop();
    public abstract void reset();

}
