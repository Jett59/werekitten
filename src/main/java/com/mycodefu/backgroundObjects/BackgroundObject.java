package com.mycodefu.backgroundObjects;

import javafx.scene.Node;
import javafx.scene.image.*;;

public class BackgroundObject implements NodeObject{
private Image img;
private String name;
BackgroundObject(Image img, String name) {
	this.setImg(img);
	this.setName(name);
}
public Image getImg() {
	return img;
}
public void setImg(Image img) {
	this.img = img;
}
@Override
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
@Override
public ImageView getImageView() {
	return new ImageView(img);
}
@Override
public Node getNode() {
	return getImageView();
}
@Override
public void move(int moveAmount) {
	var imgView = getImageView();
	imgView.setX(imgView.getX()+moveAmount);
}
}
