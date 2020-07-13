package com.mycodefu.werekitten.level;

import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.data.*;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LevelBuilder {
    private BackgroundObjectBuilder backgroundObjectBuilder;

    public LevelBuilder(BackgroundObjectBuilder backgroundObjectBuilder) {
        this.backgroundObjectBuilder = backgroundObjectBuilder;
    }

    public GameLevel buildLevel(Level level, int screenWidth, int screenHeight) {
        Size levelSize = level.getSize();

        PixelScaleHelper pixelScaleHelper = new PixelScaleHelper(levelSize.getWidth(), levelSize.getHeight(), screenWidth, screenHeight);
        for (Layer layer : level.getLayers()) {
            for (Element element : layer.getElements()) {
                Size size = element.getSize();
                size.setWidth(pixelScaleHelper.scaleX(size.getWidth()));
                size.setHeight(pixelScaleHelper.scaleY(size.getHeight()));

                Location location = element.getLocation();
                location.setX(pixelScaleHelper.scaleX(location.getX()));
                location.setY(pixelScaleHelper.scaleY(location.getY()));
            }
        }
        levelSize.setWidth(screenWidth);
        levelSize.setHeight(screenHeight);

        Map<Node, NodeObject> nodeMap = new HashMap<>();

        List<LayerGroup> layerGroups = level.getLayers().stream()
                .map(layer -> {
                    List<NodeObject> elements = layer.getElements().stream()
                            .map(backgroundObjectBuilder::build)
                            .collect(Collectors.toList());

                    for (NodeObject element : elements) {
                        nodeMap.put(element.getNode(), element);
                    }

                    Group group = new Group(elements.stream().map(NodeObject::getNode).collect(Collectors.toList()));
                    return new LayerGroup(layer.getName(), layer.getType(), group, layer.getElements(), layer.getScrollSpeed(), layer.getDepth());
                })
                .sorted(Comparator.comparingInt(LayerGroup::getDepth))
                .collect(Collectors.toList());

        return new GameLevel(level, nodeMap, layerGroups, pixelScaleHelper);
    }
    
    public GameLevel buildLevel(String levelPath, int screenWidth, int screenHeight) {
    	Level level = LevelReader.read(levelPath);
    	return buildLevel(level, screenWidth, screenHeight);
    }
}
