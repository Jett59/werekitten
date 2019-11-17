package com.mycodefu.werekitten.tools;

public class Size {
private int width;
private int height;
public int getWidth() {
	return width;
}
public void setWidth(int width) {
	this.width = width;
}
public int getHeight() {
	return height;
}
public void setHeight(int height) {
	this.height = height;
}
public Size() {

}
public Size(int width, int height) {
	setWidth(width);
	setHeight(height);
}
}
