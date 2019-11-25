package com.mycodefu.werekitten.slide;

import java.util.ArrayList;
import java.util.List;

import com.mycodefu.werekitten.backgroundObjects.NodeObject;

import javafx.scene.Group;
import javafx.scene.Node;

import java.util.stream.*;

public class SlideBackground {
    private List<LayerGroup> layerGroups = new ArrayList<>();
	private Group parentGroup = new Group();

    private SlideBackground() {
    }

    public void addLayerGroup(LayerGroup layerGroup) {
        layerGroups.add(layerGroup);
		parentGroup.getChildren().add(layerGroup.getGroup());
    }

    public Group getParentGroup() {
    	return parentGroup;
	}

    public void moveX(int moveAmount) {
        for (LayerGroup layerGroup : layerGroups) {
            Group group = layerGroup.getGroup();
            group.setTranslateX(group.getTranslateX() + moveAmount * layerGroup.getSpeed());
        }
    }

    public static SlideBackground empty() {
        return new SlideBackground();
    }
}
