package com.mycodefu.werekitten.level.designer;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.level.data.Element;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.Node;
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

    private NodeObject selectedNode;
    private Label details;

    public Scene getScene() {
        Group levelGroup = buildLevelGroup();
        Group toolGroup = buildToolGroup();
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
        border.setBackground(new Background(new BackgroundFill(Color.CYAN, null, null)));



        border.setTop(details);
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
