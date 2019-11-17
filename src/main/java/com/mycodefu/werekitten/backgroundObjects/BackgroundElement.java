package com.mycodefu.werekitten.backgroundObjects;

import com.mycodefu.werekitten.tools.Location;
import com.mycodefu.werekitten.tools.Size;

public class BackgroundElement {
private String type;
private Size size;
private Location location;
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public Size getSize() {
	return size;
}
public void setSize(Size size) {
	this.size = size;
}
public Location getLocation() {
	return location;
}
public void setLocation(Location location) {
	this.location = location;
}
public BackgroundElement() {

}
public BackgroundElement(String type, Location location, Size size) {
	setLocation(location);
	setSize(size);
}
}
