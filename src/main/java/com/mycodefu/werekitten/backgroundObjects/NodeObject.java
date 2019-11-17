package com.mycodefu.werekitten.backgroundObjects;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

public interface NodeObject {
public Node getNode();
public void move(int moveAmount);
public String getName();
public ImageView getImageView();
}
