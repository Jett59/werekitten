package com.mycodefu.werekitten.backgroundObjects;

import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.level.data.Element;

import javafx.scene.Node;
import javafx.scene.shape.Shape;

public class BackgroundAnimationObject implements NodeObject {
    private Animation animation;
    private String name;
private Element element;
    
    public BackgroundAnimationObject(Animation animation, String name, Element element) {
        this.animation = animation;
        this.name = name;
        this.element = element;
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

	@Override
	public Element getDataElement() {
		return element;
	}
}
