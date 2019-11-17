package com.mycodefu.werekitten.backgroundObjects;

import com.mycodefu.werekitten.animation.*;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class AnimatedBackgroundObject implements NodeObject{
private Animation animat;
private String name;
public AnimatedBackgroundObject(Animation animat, String name) {
	this.animat = animat;
	this.name = name;
}
	@Override
	public Node getNode() {
		return animat.getImageView();
	}

	@Override
	public void move(int moveAmount) {
		animat.getImageView().setX(animat.getImageView().getX()+moveAmount);
	}
	@Override
	public String getName() {
		return name;
	}

@Override
public ImageView getImageView() {
	return animat.getImageView();
}
}
