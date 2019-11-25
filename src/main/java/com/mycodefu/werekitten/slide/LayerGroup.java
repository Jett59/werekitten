package com.mycodefu.werekitten.slide;

import javafx.scene.Group;

public class LayerGroup {
    private String name;
    private Group group;
    private double speed;
    private int depth;

    public LayerGroup(String name, Group group, double speed, int depth) {
        this.name = name;
        this.group = group;
        this.speed = speed;
        this.depth = depth;
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDepth() {
        return depth;
    }
}
