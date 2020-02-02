package com.mycodefu.werekitten.slide;

import java.util.List;

import com.mycodefu.werekitten.level.data.Element;
import com.mycodefu.werekitten.level.data.LayerType;

import javafx.scene.Group;

public class LayerGroup {
    private String name;
    private LayerType layerType;
    private Group group;
    private List<Element> elements;
    private double speed;
    private int depth;

    public LayerGroup(String name, LayerType layerType, Group group, List<Element> elements, double speed, int depth) {
        this.name = name;
        this.layerType = layerType;
        this.group = group;
        this.elements = elements;
        this.speed = speed;
        this.depth = depth;
    }

    public String getName() {
        return name;
    }
    
    public LayerType getLayerType() {
    	return layerType;
    }

    public Group getGroup() {
        return group;
    }
    
    public List<Element> getElements(){
    	return elements;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDepth() {
        return depth;
    }
}
