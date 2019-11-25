package com.mycodefu.werekitten.backgroundObjects;

import com.mycodefu.werekitten.animation.Animation;
import javafx.scene.Node;

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
    public String getName() {
        return name;
    }

	public Animation getAnimation() {
		return animation;
	}
}
