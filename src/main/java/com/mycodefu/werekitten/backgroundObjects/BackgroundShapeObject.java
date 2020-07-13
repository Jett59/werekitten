package com.mycodefu.werekitten.backgroundObjects;

import com.mycodefu.werekitten.level.data.Element;

import javafx.scene.Node;
import javafx.scene.shape.Shape;

public class BackgroundShapeObject implements NodeObject {
    private String name;
    private Shape shape;
private Element element;
    
    public BackgroundShapeObject(String name, Shape shape, Element element) {
        this.name = name;
        this.shape = shape;
        this.element = element;
    }

    @Override
    public Node getNode() {
        return shape;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

	@Override
	public Element getDataElement() {
		return element;
	}
}
