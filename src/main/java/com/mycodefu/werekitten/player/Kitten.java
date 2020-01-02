package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.animation.AnimationCompiler;

import javafx.animation.Interpolator;
import javafx.util.Duration;

public class Kitten extends Player{

private Kitten(Animation idleRight, Animation idleLeft, Animation walkRight, Animation walkLeft, int jumpAmount) {
	super(idleRight, idleLeft, walkRight, walkLeft, jumpAmount);
}

private static void addPropertiesToAnimation(Animation animation, int cycleCount, Interpolator interpolator) {
	animation.setCycleCount(cycleCount);
	animation.setInterpolator(interpolator);
}

public static Kitten create(int jumpAmount, int height, Duration duration) {
	AnimationCompiler animationCompiler = new AnimationCompiler();
	
	Animation idleRight = animationCompiler.compileAnimation("cat", "Idle", 10, duration, false, height, "height");
	
	Animation idleLeft = animationCompiler.compileAnimation("cat", "Idle", 10, duration, true, height, "height");
	Animation walkRight = animationCompiler.compileAnimation("cat", "walk", 10, duration, false, height, "height");
	Animation walkLeft = animationCompiler.compileAnimation("cat", "walk", 10, duration, true, height, "height");
	
	addPropertiesToAnimation(idleRight, -1, Interpolator.LINEAR);
	addPropertiesToAnimation(idleLeft, -1, Interpolator.LINEAR);
	addPropertiesToAnimation(walkRight, 1, Interpolator.LINEAR);
	addPropertiesToAnimation(walkLeft, 1, Interpolator.LINEAR);
	
	Kitten result = new Kitten(idleRight, idleLeft, walkRight, walkLeft, jumpAmount);
	return result;
}

}