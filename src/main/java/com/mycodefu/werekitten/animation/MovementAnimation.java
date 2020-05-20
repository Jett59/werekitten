package com.mycodefu.werekitten.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class MovementAnimation extends Transition {
	public static double pixelRate = 120;

    private Node node;
    private double x = Double.MAX_VALUE;
    private double rightBound;
    private MovementDirection direction;
    private double distanceToEdge = Double.MAX_VALUE;
private double lastFrac = 0;
    
    @Override
    protected void interpolate(double frac) {
        switch (direction) {
        case Left: {
        	if(distanceToEdge == Double.MAX_VALUE) {
        		distanceToEdge = node.getTranslateX();
        	}
        	if(frac > lastFrac) {
        	x = distanceToEdge*(1-frac);
        	node.setTranslateX(x);
        	lastFrac = frac;
        	}
        	break;
        }
		case Right:{
			if(distanceToEdge == Double.MAX_VALUE) {
				distanceToEdge = rightBound-node.getTranslateX();
			}
			if(frac > lastFrac) {
				x = rightBound-distanceToEdge*(1-frac);
				node.setTranslateX(x);
				lastFrac = frac;
			}
		}
		default:
			break;
        }
    }

    public MovementAnimation(Node node, double pixelRate, MovementDirection initialDirection, double rightBound, Duration duration) {
        this.node = node;
        this.rightBound = rightBound;
        this.direction = initialDirection;
        setInterpolator(Interpolator.LINEAR);
        setCycleCount(1);
    }

    @Override
    public void play() {
        x = node.getTranslateX();
        lastFrac = 0;
        distanceToEdge = Double.MAX_VALUE;
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
        switch (direction) {
        case Left: {
        	super.setCycleDuration(Duration.millis((node.getTranslateX()/pixelRate)*1000));
        	break;
        }
        case Right: {
        	super.setCycleDuration(Duration.millis((rightBound-node.getTranslateX())/pixelRate*1000));
        }
        }
    }

    public enum MovementDirection {
        Left,
        Right
    }
}
