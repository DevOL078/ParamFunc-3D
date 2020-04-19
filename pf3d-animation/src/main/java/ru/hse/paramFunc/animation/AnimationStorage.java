package ru.hse.paramFunc.animation;

import java.util.List;

public class AnimationStorage {

    private static final List<Animation> animations;

    static {
        animations = List.of(
                new FlyingPointAnimation("Flying point"),
                new DynamicLinesAnimation("Dynamic lines"));
    }

    public static List<Animation> getAnimations() {
        return List.copyOf(animations);
    }

}
