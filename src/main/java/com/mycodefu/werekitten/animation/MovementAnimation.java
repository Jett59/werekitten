package com.mycodefu.werekitten.animation;

import java.util.function.Function;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class MovementAnimation extends Transition {
    public static final double TARGET_FRAME_RATE = 60d;

    private Node node;
    private double x = Double.MAX_VALUE;
    private double pixelRate;
    private Function<Double, Boolean> boundChecker;

    @Override
    protected void interpolate(double frac) {
        if (boundChecker.apply(x + pixelRate)) {
            x += pixelRate;
            node.setTranslateX(x);
        }
    }

    public MovementAnimation(Node node, double pixelRate, Function<Double, Boolean> boundChecker, Duration duration) {
        super(TARGET_FRAME_RATE);
        this.node = node;
        this.pixelRate = pixelRate;
        this.boundChecker = boundChecker;
        setCycleDuration(duration);
        setCycleCount(-1);
    }

    @Override
    public void play() {
        x = node.getTranslateX();
        super.play();
    }
}
