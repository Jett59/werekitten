package com.mycodefu.werekitten.backgroundObjects;

import com.mycodefu.werekitten.level.data.Element;

import javafx.scene.Node;
import javafx.scene.shape.Shape;

public interface NodeObject {
    String getName();
    Node getNode();
    Shape getShape();
    Element getDataElement();
}
