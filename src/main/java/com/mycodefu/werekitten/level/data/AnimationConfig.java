package com.mycodefu.werekitten.level.data;

public class AnimationConfig {
private int durationMillis;
private int framesInAnimation;
private boolean reversed;
public int getDurationMillis() {
	return durationMillis;
}
public void setDurationMillis(int durationMillis) {
	this.durationMillis = durationMillis;
}
public int getFramesInAnimation() {
	return framesInAnimation;
}
public void setFramesInAnimation(int framesInAnimation) {
	this.framesInAnimation = framesInAnimation;
}
public boolean getReversed() {
	return reversed;
}
public void setReversed(boolean reversed) {
	this.reversed = reversed;
}
public AnimationConfig(int durationMillis, int framesInAnimation, boolean reversed) {
	setDurationMillis(durationMillis);
	setFramesInAnimation(framesInAnimation);
	setReversed(reversed);
}
public AnimationConfig() {
	
}
}
