package com.mycodefu.werekitten.slide;

import java.util.ArrayList;
import java.util.List;

import com.mycodefu.werekitten.backgroundObjects.BackgroundElement;

public class Layer {
private String name;
private double scrollSpeed;
private List<BackgroundElement> elements = new ArrayList<>();
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public double getScrollSpeed() {
	return scrollSpeed;
}
public void setScrollSpeed(double scrollSpeed) {
	this.scrollSpeed = scrollSpeed;
}
public List<BackgroundElement> getElements() {
	return elements;
}
public void setElements(List<BackgroundElement> elements) {
	this.elements = elements;
}
public Layer() {

}
public Layer(String name, double scrollSpeed, List<BackgroundElement> elements) {
	setName(name);
	setScrollSpeed(scrollSpeed);
	setElements(elements);
}
public void addElement(BackgroundElement elem) {
	elements.add(elem);
}
public void removeElement(BackgroundElement elem) {
	elements.remove(elem);
}
}
