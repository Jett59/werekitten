package com.mycodefu.werekitten.backgroundObjects;

import javafx.scene.Node;
import javafx.scene.image.*;;

public class BackgroundObject implements NodeObject {
    private final ImageView imageView;
    private Image image;
    private String name;

    BackgroundObject(Image image, String name) {
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

    @Override
    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public Node getNode() {
        return imageView;
    }

    @Override
    public void move(int moveAmount) {
        var imgView = getImageView();
        imgView.setX(imgView.getX() + moveAmount);
    }
}
