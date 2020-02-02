package com.mycodefu.werekitten.level;

import com.mycodefu.werekitten.level.data.*;
import com.mycodefu.werekitten.slide.LayerGroup;

import java.util.ArrayList;
import java.util.List;

public class GameLevel {
    private Level levelData;
    private List<LayerGroup> layerGroups;

    public GameLevel(Level levelData, List<LayerGroup> layerGroups) {
        this.levelData = levelData;
        this.layerGroups = layerGroups;
    }

    public Level getLevelData() {
        return levelData;
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
