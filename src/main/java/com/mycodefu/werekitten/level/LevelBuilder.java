package com.mycodefu.werekitten.level;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.data.Level;
import com.mycodefu.werekitten.slide.LayerGroup;

import javafx.scene.Group;

public class LevelBuilder {
	private BackgroundObjectBuilder backgroundObjectBuilder;
	
public LevelBuilder(BackgroundObjectBuilder backgroundObjectBuilder) {
	this.backgroundObjectBuilder = backgroundObjectBuilder;
}

public List<LayerGroup> buildLevel(String LevelPath){
	Level defaultLevel = LevelReader.read(LevelPath);
    return defaultLevel.getLayers().stream()
            .map(layer -> {
                List<NodeObject> elements = layer.getElements().stream().map(backgroundElement -> {
                    NodeObject nodeObject = backgroundObjectBuilder.build(backgroundElement);
                    return nodeObject;
                }).collect(Collectors.toList());

                Group group = new Group(elements.stream().map(NodeObject::getNode).collect(Collectors.toList()));
                return new LayerGroup(layer.getName(), group, layer.getScrollSpeed(), layer.getDepth());
            })
            .sorted(Comparator.comparingInt(LayerGroup::getDepth))
            .collect(Collectors.toList());
}
}
