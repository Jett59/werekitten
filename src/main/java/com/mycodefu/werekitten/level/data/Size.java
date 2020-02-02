package com.mycodefu.werekitten.level.data;

public class Size {
private double width;
private double height;
public double getWidth() {
	return width;
}
public void setWidth(double width) {
	this.width = width;
}
public double getHeight() {
	return height;
}
public void setHeight(double height) {
	this.height = height;
}
public Size() {

}
public Size(double width, double height) {
	setWidth(width);
	setHeight(height);
}
}
