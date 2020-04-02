package com.mycodefu.werekitten.animation;

import java.util.function.Function;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class MovementAnimation extends Transition{
private Node node;
private double pixelRate;
private Function<Double, Boolean> boundChecker;
private double fracIncrement = Double.MAX_VALUE;
private double pixelIncrement;

	@Override
	protected void interpolate(double frac) {
		if(fracIncrement == Double.MAX_VALUE) {
			if(frac > 0d) {
				fracIncrement = frac;
				System.out.println("movement animation frame rate: "+1/fracIncrement);
				System.out.println("frac increment: "+fracIncrement);
			}
		}else {
			pixelIncrement = fracIncrement*pixelRate;
			System.out.println("pixel increment: "+pixelIncrement);
			if(boundChecker.apply(node.getTranslateX()+pixelIncrement)) {
				node.setTranslateX(node.getTranslateX()+pixelIncrement);
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
}
