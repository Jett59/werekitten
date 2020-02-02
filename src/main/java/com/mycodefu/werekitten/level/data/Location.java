package com.mycodefu.werekitten.level.data;

public class Location{
private double x;
private double y;
public double getX() {
	return x;
}
public void setX(double x) {
	this.x = x;
}
public double getY() {
	return y;
}
public void setY(double y) {
	this.y = y;
}
public Location() {

}
public Location(double x, double y) {
	setX(x);
	setY(y);
}
}
