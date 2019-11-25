package com.mycodefu.werekitten.backgroundObjects;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;

public class BackgroundShapeObject implements NodeObject {
    private String name;
    private Shape shape;

    public BackgroundShapeObject(String name, Shape shape) {
        this.name = name;
        this.shape = shape;
    }

    @Override
    public Node getNode() {
        return shape;
    }

    @Override
    public String getName() {
        return name;
    }

}
