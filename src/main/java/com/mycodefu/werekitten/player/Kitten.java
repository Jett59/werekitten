package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.animation.AnimationCompiler;

import com.mycodefu.werekitten.level.PixelScaleHelper;
import javafx.animation.Interpolator;
import javafx.util.Duration;

public class Kitten extends Player{
	public static final double KITTEN_SPEED = 120d;

private Kitten(String id, Animation idleRight, Animation idleLeft, Animation walkRight, Animation walkLeft, double jumpAmount, AnimationType initialAnimation, double initialXPosition, double speed) {
	super(id, idleRight, idleLeft, walkRight, walkLeft, jumpAmount, initialAnimation, initialXPosition, speed);
}

private static void addPropertiesToAnimation(Animation animation, int cycleCount, Interpolator interpolator) {
	animation.setCycleCount(cycleCount);
	animation.setInterpolator(interpolator);
}

public static Kitten create(String id, double jumpAmount, double height, Duration duration, AnimationType initialAnimation, double initialXPosition, PixelScaleHelper pixelScaleHelper) {
	AnimationCompiler animationCompiler = new AnimationCompiler();
	
	Animation idleRight = animationCompiler.compileAnimation("cat", "Idle", 10, duration, false, height, "height");
	
	Animation idleLeft = animationCompiler.compileAnimation("cat", "Idle", 10, duration, true, height, "height");
	Animation walkRight = animationCompiler.compileAnimation("cat", "Walk", 10, duration, false, height, "height");
	Animation walkLeft = animationCompiler.compileAnimation("cat", "Walk", 10, duration, true, height, "height");
	
	addPropertiesToAnimation(idleRight, -1, Interpolator.LINEAR);
	addPropertiesToAnimation(idleLeft, -1, Interpolator.LINEAR);
	addPropertiesToAnimation(walkRight, -1, Interpolator.LINEAR);
	addPropertiesToAnimation(walkLeft, -1, Interpolator.LINEAR);

	double speed = pixelScaleHelper.scaleX(KITTEN_SPEED);

	Kitten result = new Kitten(id, idleRight, idleLeft, walkRight, walkLeft, jumpAmount, initialAnimation, initialXPosition, speed);
	return result;
}


}
