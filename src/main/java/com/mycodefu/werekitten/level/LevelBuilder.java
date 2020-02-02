package com.mycodefu.werekitten.level;

import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.data.*;
import com.mycodefu.werekitten.position.PixelScaleHelper;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LevelBuilder {
    private BackgroundObjectBuilder backgroundObjectBuilder;

    public LevelBuilder(BackgroundObjectBuilder backgroundObjectBuilder) {
        this.backgroundObjectBuilder = backgroundObjectBuilder;
    }

    public GameLevel buildLevel(String LevelPath, int screenWidth, int screenHeight) {
        Level defaultLevel = LevelReader.read(LevelPath);

        Size levelSize = defaultLevel.getSize();

        PixelScaleHelper pixelScaleHelper = new PixelScaleHelper(levelSize.getWidth(), levelSize.getHeight(), screenWidth, screenHeight);
        for (Layer layer : defaultLevel.getLayers()) {
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

        List<LayerGroup> layerGroups = defaultLevel.getLayers().stream()
                .map(layer -> {
                    List<NodeObject> elements = layer.getElements().stream().map(backgroundElement -> {
                        NodeObject nodeObject = backgroundObjectBuilder.build(backgroundElement);
                        return nodeObject;
                    }).collect(Collectors.toList());

                    Group group = new Group(elements.stream().map(NodeObject::getNode).collect(Collectors.toList()));
                    return new LayerGroup(layer.getName(), layer.getType(), group, layer.getElements(), layer.getScrollSpeed(), layer.getDepth());
                })
                .sorted(Comparator.comparingInt(LayerGroup::getDepth))
                .collect(Collectors.toList());

        return new GameLevel(defaultLevel, layerGroups, pixelScaleHelper);
    }
}
