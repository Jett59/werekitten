package com.mycodefu.werekitten.level.designer;

import java.util.stream.Collectors;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.slide.LayerGroup;

import javafx.scene.Group;
import javafx.scene.Scene;

public class LevelDesignerUI {
public static int LEVEL_WIDTH = 1024;
public static int LEVEL_HEIGHT = 768;

public Scene getScene() {
	GameLevel level = new LevelBuilder(new BackgroundObjectBuilder(new AnimationCompiler())).buildLevel("/level.wkl", LEVEL_WIDTH, LEVEL_HEIGHT);
	Group combined = new Group(level.getLayerGroups().stream()
	.map(LayerGroup::getGroup)
	.collect(Collectors.toList()));
	return new Scene(combined);
}
}
