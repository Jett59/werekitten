package com.mycodefu.werekitten.position;

public class PixelScaleHelper {
private int scaleWidth, scaleHeight;
private int screenWidth, screenHeight;

public PixelScaleHelper(int scaleWidth, int scaleHeight, int screenWidth, int screenHeight) {
	this.scaleHeight = scaleHeight;
	this.scaleWidth = scaleWidth;
	this.screenHeight = screenHeight;
	this.screenWidth = screenWidth;
}

public int scaleX(int x) {
	double ratio = (double)x/(double)scaleWidth;
	int result = (int) Math.ceil(ratio*screenWidth);
	return result;
}

public int scaleY(int y) {
	double ratio = (double)y/(double)scaleHeight;
	int result = (int) Math.ceil(ratio*screenHeight);
	return result;
}
}
