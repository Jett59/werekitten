package com.mycodefu.werekitten.backgroundObjects;

import javafx.scene.Node;
import javafx.scene.image.*;;

public class BackgroundImageObject implements NodeObject {
    private final ImageView imageView;
    private Image image;
    private String name;

    BackgroundImageObject(Image image, String name) {
        this.image = image;
        this.imageView = new ImageView(image);
        this.name = name;
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

    @Override
    public Node getNode() {
        return imageView;
    }

}
