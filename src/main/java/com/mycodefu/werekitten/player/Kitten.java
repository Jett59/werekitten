package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.animation.AnimationCompiler;

import javafx.animation.Interpolator;
import javafx.util.Duration;

public class Kitten extends Player{
	public static int MOVE_AMOUNT = 2;

private Kitten(Animation idleRight, Animation idleLeft, Animation walkRight, Animation walkLeft, int jumpAmount, AnimationType initialAnimation, int initialXPosition) {
	super(idleRight, idleLeft, walkRight, walkLeft, jumpAmount, initialAnimation, initialXPosition);
	System.out.println("created the kitten");
	if(equals(null)) {
		System.out.println("kitten == null");
	}else {
		System.out.println("kitten != null");
	}
	if(super.equals(null)) {
		System.out.println("player == null");
	}else {
		System.out.println("player != null");
	}
}

private static void addPropertiesToAnimation(Animation animation, int cycleCount, Interpolator interpolator) {
	animation.setCycleCount(cycleCount);
	animation.setInterpolator(interpolator);
}

public static Kitten create(int jumpAmount, int height, Duration duration, AnimationType initialAnimation, int initialXPosition) {
	AnimationCompiler animationCompiler = new AnimationCompiler();
	
	Animation idleRight = animationCompiler.compileAnimation("cat", "Idle", 10, duration, false, height, "height");
	
	Animation idleLeft = animationCompiler.compileAnimation("cat", "Idle", 10, duration, true, height, "height");
	Animation walkRight = animationCompiler.compileAnimation("cat", "Walk", 10, duration, false, height, "height");
	Animation walkLeft = animationCompiler.compileAnimation("cat", "Walk", 10, duration, true, height, "height");
	
	addPropertiesToAnimation(idleRight, -1, Interpolator.LINEAR);
	addPropertiesToAnimation(idleLeft, -1, Interpolator.LINEAR);
	addPropertiesToAnimation(walkRight, 1, Interpolator.LINEAR);
	addPropertiesToAnimation(walkLeft, 1, Interpolator.LINEAR);
	
	Kitten result = new Kitten(idleRight, idleLeft, walkRight, walkLeft, jumpAmount, initialAnimation, initialXPosition);
	return result;
}


}
