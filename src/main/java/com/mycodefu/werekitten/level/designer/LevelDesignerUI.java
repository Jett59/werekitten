package com.mycodefu.werekitten.level.designer;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class LevelDesignerUI {
    public static int LEVEL_WIDTH = 1024;
    public static int TOOL_WIDTH = 250;
    public static int LEVEL_HEIGHT = 768;
    private GameLevel level;
    private Group levelGroup;
    private Group toolGroup;

    private NodeObject selectedNode;
    private Label details;
private Button apply;
    
    public Scene getScene() {
        levelGroup = buildLevelGroup();
        toolGroup = buildToolGroup();
        Group root = new Group(levelGroup, toolGroup);
        createHandlers(toolGroup, levelGroup);
        Scene scene = new Scene(root);

        return scene;
    }

    private Group buildToolGroup() {
        details = new Label("Details");
        BorderPane border = new BorderPane();
        border.setLayoutX(LEVEL_WIDTH);
        border.setPrefWidth(TOOL_WIDTH);
        border.setPrefHeight(LEVEL_HEIGHT-100);
        border.setBackground(new Background(new BackgroundFill(Color.CYAN, null, null)));

apply = new Button("apply");
apply.setOnAction(e->{
	levelGroup.getChildren().clear();
	GameLevel newLevel = new LevelBuilder(new BackgroundObjectBuilder(new AnimationCompiler())).buildLevel(level.getLevelData(), LEVEL_WIDTH, LEVEL_HEIGHT);
	level = newLevel;
	levelGroup.getChildren().addAll(newLevel.getLayerGroups().stream()
			.map(LayerGroup::getGroup)
			.collect(Collectors.toList()));
});

        border.setTop(details);
        border.setBottom(apply);
        return new Group(border);
    }

    private Group buildLevelGroup() {
        level = new LevelBuilder(new BackgroundObjectBuilder(new AnimationCompiler())).buildLevel("/level.wkl", LEVEL_WIDTH, LEVEL_HEIGHT);
        return new Group(level.getLayerGroups().stream()
                .map(LayerGroup::getGroup)
                .collect(Collectors.toList()));
    }
    
    public void createHandlers(Group toolGroup, Group levelGroup) {
    	levelGroup.setOnMouseClicked(e->{
    		selectedNode = level.getNodeMap().get(e.getTarget());
            String detailsHeading = String.format("Selected: %s", selectedNode.getDataElement().getName());
            details.setText(detailsHeading);
    		System.out.println(detailsHeading);
    	});
    }
}
