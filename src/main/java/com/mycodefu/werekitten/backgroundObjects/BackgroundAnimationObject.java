package com.mycodefu.werekitten.backgroundObjects;

import com.mycodefu.werekitten.animation.Animation;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

public class BackgroundAnimationObject implements NodeObject {
    private Animation animation;
    private String name;

    public BackgroundAnimationObject(Animation animation, String name) {
        this.animation = animation;
        this.name = name;
    }

    @Override
    public Node getNode() {
        return animation.getImageView();
    }

    @Override
    public Shape getShape() {
        return animation.getCurrentShape();
    }

    @Override
    public String getName() {
        return name;
    }

	public Animation getAnimation() {
		return animation;
	}
}
