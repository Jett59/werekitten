package com.mycodefu.werekitten.animation;

import java.util.function.Function;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class MovementAnimation extends Transition{
private Node node;
private double x = Double.MAX_VALUE;
private double pixelRate;
private Function<Double, Boolean> boundChecker;
private double fracIncrement = Double.MAX_VALUE;
private double pixelIncrement;

	@Override
	protected void interpolate(double frac) {
		if(x == Double.MAX_VALUE) {
			x = node.getTranslateX();
		}
		if(fracIncrement == Double.MAX_VALUE) {
			if(frac > 0d) {
				fracIncrement = frac;
				System.out.println("movement animation frame rate: "+1/fracIncrement);
				System.out.println("frac increment: "+fracIncrement);
			}
		}else {
			pixelIncrement = fracIncrement*pixelRate;
			if(boundChecker.apply(x+pixelIncrement)) {
				x+=pixelIncrement;
				node.setTranslateX(x);
			}
		}
	}

public MovementAnimation(Node node, double pixelRate, Function<Double, Boolean> boundChecker, Duration duration) {
	this.node = node;
	this.pixelRate = pixelRate;
	this.boundChecker = boundChecker;
	setCycleDuration(duration);
	setCycleCount(-1);
}

@Override
	public void play() {
x = Double.MAX_VALUE;
		super.play();
	}
}
