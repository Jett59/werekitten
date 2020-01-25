package com.mycodefu.werekitten.position;

import javafx.stage.Screen;

public class PixelPercentageHelper {
	private int width, height;
private PixelPercentageHelper(int width, int height) {
	this.height = height;
	this.width = width;
}

public int getWidthPixelPosition(double widthPercentage) {
	return (int) (width*widthPercentage);
}

public int getHeightPixelPosition(double heightPixelPercentage) {
	return (int) (height*heightPixelPercentage);
}

public static PixelPercentageHelper init() {
	Screen screen = Screen.getPrimary();
	return new PixelPercentageHelper((int) screen.getBounds().getWidth(), (int)screen.getBounds().getHeight());
}

}