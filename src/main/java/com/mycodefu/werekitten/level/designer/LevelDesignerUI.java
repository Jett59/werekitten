package com.mycodefu.werekitten.level.designer;

import javafx.scene.Group;
import javafx.scene.Scene;

public class LevelDesignerUI {
public static double LEVEL_WIDTH = 1024;
public static double LEVEL_HEIGHT = 768;

public Scene getScene() {
	Group group = new Group();
	return new Scene(group);
}
}
