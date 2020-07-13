package com.mycodefu.werekitten.level;

import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.data.*;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Node;

import java.util.List;
import java.util.Map;

public class GameLevel {
    private Level levelData;
    private Map<Node, NodeObject> nodeMap;
    private List<LayerGroup> layerGroups;
    private PixelScaleHelper pixelScaleHelper;

    public GameLevel(Level levelData, Map<Node, NodeObject> nodeMap, List<LayerGroup> layerGroups, PixelScaleHelper pixelScaleHelper) {
        this.levelData = levelData;
        this.nodeMap = nodeMap;
        this.layerGroups = layerGroups;
        this.pixelScaleHelper = pixelScaleHelper;
    }

    public Map<Node, NodeObject> getNodeMap() {
        return nodeMap;
    }

    public Level getLevelData() {
        return levelData;
    }

    public PixelScaleHelper getPixelScaleHelper() {
        return pixelScaleHelper;
    }

    public List<LayerGroup> getLayerGroups() {
        return layerGroups;
    }

    public Element getPlayerElement(){
		for (Layer layer : this.levelData.getLayers()) {
			if (layer.getType()== LayerType.Player){
				return layer.getElements().get(0);
			}
		}
		throw new IllegalArgumentException("No Player layer was included in the level WKL.");
	}
}
