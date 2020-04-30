package ru.hse.paramFunc.animation;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<Animation> getCopyAnimations() {
        return animations.stream()
                .map(Animation::copy)
                .collect(Collectors.toList());
    }

    public static Animation getCopyOf(String animationName) {
        Animation animation = animations.stream()
                .filter(a -> a.getName().equals(animationName))
                .findAny()
                .orElseThrow();
        return animation.copy();
    }

}
