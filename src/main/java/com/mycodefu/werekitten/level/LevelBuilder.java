package com.mycodefu.werekitten.level;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.data.*;
import com.mycodefu.werekitten.position.PixelScaleHelper;
import com.mycodefu.werekitten.slide.LayerGroup;

import javafx.scene.Group;

public class LevelBuilder {
	private BackgroundObjectBuilder backgroundObjectBuilder;
	
public LevelBuilder(BackgroundObjectBuilder backgroundObjectBuilder) {
	this.backgroundObjectBuilder = backgroundObjectBuilder;
}

public List<LayerGroup> buildLevel(String LevelPath){
	Level defaultLevel = LevelReader.read(LevelPath);

    Size levelSize = defaultLevel.getSize();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    PixelScaleHelper pixelScaleHelper = new PixelScaleHelper(levelSize.getWidth(), levelSize.getHeight(), screenSize.width, screenSize.height);
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

    return defaultLevel.getLayers().stream()
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
}
}
