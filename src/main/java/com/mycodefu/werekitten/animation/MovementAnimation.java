package com.mycodefu.werekitten.animation;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class MovementAnimation extends Transition {
    public static final double TARGET_FRAME_RATE = 60d;

    private Node node;
    private double x = Double.MAX_VALUE;
    private double pixelRate;
    private double rightBound;
    private MovementDirection direction;

    @Override
    protected void interpolate(double frac) {
        if (direction == MovementDirection.Left && x - pixelRate >= 0) {
            x -= pixelRate;
            node.setTranslateX(x);
        } else if (direction == MovementDirection.Right && x + pixelRate < rightBound) {
            x += pixelRate;
            node.setTranslateX(x);
        }
    }

    public MovementAnimation(Node node, double pixelRate, MovementDirection initialDirection, double rightBound, Duration duration) {
        super(TARGET_FRAME_RATE);
        this.node = node;
        this.pixelRate = pixelRate;
        this.rightBound = rightBound;
        this.direction = initialDirection;
        setCycleDuration(duration);
        setCycleCount(-1);
    }

    @Override
    public void play() {
        x = node.getTranslateX();
        super.play();
    }

    public void syncX(double x){
        this.x = x;
        node.setTranslateX(x);
    }

    public MovementDirection getDirection() {
    	return direction;
    }
    
    public void setDirection(MovementDirection direction){
        this.direction = direction;
    }

    public enum MovementDirection {
        Left,
        Right
    }
}
