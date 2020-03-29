package com.mycodefu.werekitten.animation;

import java.util.Collection;

import com.mycodefu.werekitten.pipeline.Pipeline;

import javafx.animation.Transition;
import javafx.util.Duration;

public class GameLoopAnimation extends Transition{
private Collection<Pipeline> pipelines;
private int preferredFrameRate;
private double nextFrac;

	@Override
	protected void interpolate(double frac) {
		if(frac == 1d) {
			nextFrac = 1d/preferredFrameRate;
		}
		if(nextFrac <= frac) {
			nextFrac += (1d/preferredFrameRate);
			for(Pipeline pipeline : pipelines) {
				pipeline.tick();
			}
		}
	}

public GameLoopAnimation(Collection<Pipeline> pipelines, int preferredFrameRate, Duration cycleDuration) {
	setCycleDuration(cycleDuration);
	this.pipelines = pipelines;
	this.preferredFrameRate = preferredFrameRate;
}
}
