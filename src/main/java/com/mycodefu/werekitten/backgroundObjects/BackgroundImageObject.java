package com.mycodefu.werekitten.backgroundObjects;

import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;;

public class BackgroundImageObject implements NodeObject {
    private final ImageView imageView;
    private Image image;
    private String name;
    private Polygon polygon;

    BackgroundImageObject(Image image, String name, Polygon polygon) {
        this.image = image;
        this.imageView = new ImageView(image);
        this.name = name;
        this.polygon = polygon;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String getName() {
        return name;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    @Override
    public Shape getShape() {
        return polygon;
    }

    @Override
    public Node getNode() {
        return imageView;
    }

}
