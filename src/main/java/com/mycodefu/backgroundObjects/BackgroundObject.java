package com.mycodefu.backgroundObjects;

import javafx.scene.image.*;;

public class BackgroundObject {
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
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public ImageView getImageView() {
	return new ImageView(img);
}
}
