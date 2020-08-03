package com.mycodefu.werekitten.level.designer;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.files.LocalFileStorage;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.level.LevelSerializer;
import com.mycodefu.werekitten.level.data.Element;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
    private TextField xBox;
    private TextField yBox;
    private TextField widthBox;
    private TextField heightBox;
    private Button apply;
private Button save;
    
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
        xBox = new TextField();
        yBox = new TextField();
        widthBox = new TextField();
        heightBox = new TextField();
        BorderPane border = new BorderPane();
        border.setLayoutX(LEVEL_WIDTH);
        border.setPrefWidth(TOOL_WIDTH);
        border.setPrefHeight(LEVEL_HEIGHT - 100);
        border.setBackground(new Background(new BackgroundFill(Color.CYAN, null, null)));

        apply = new Button("apply");
        apply.setOnAction(e -> {
            Element dataElement = selectedNode.getDataElement();
            dataElement.getLocation().setX(Double.parseDouble(this.xBox.getText()));
            dataElement.getLocation().setY(Double.parseDouble(this.yBox.getText()));
            dataElement.getSize().setWidth(Double.parseDouble(this.widthBox.getText()));
            dataElement.getSize().setHeight(Double.parseDouble(this.heightBox.getText()));

            levelGroup.getChildren().clear();
            GameLevel newLevel = new LevelBuilder(new BackgroundObjectBuilder(new AnimationCompiler())).buildLevel(level.getLevelData(), LEVEL_WIDTH, LEVEL_HEIGHT);
            level = newLevel;
            levelGroup.getChildren().addAll(newLevel.getLayerGroups().stream()
                    .map(LayerGroup::getGroup)
                    .collect(Collectors.toList()));
        });
        save = new Button("save");
        save.setOnAction(e->{
        	try {
        	LocalFileStorage.writeLevelFile(level.getLevelData().getName(), LevelSerializer.serialize(level.getLevelData()));
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
        });
        VBox detailsHeadingAndSettings = new VBox(10, details, new Text("X"), xBox, new Text("Y"), yBox, new Text("Width"), widthBox, new Text("Height"), heightBox);
        HBox applyAndSave = new HBox(50, apply, save);
        border.setTop(detailsHeadingAndSettings);
        border.setBottom(applyAndSave);
        return new Group(border);
    }

    private Group buildLevelGroup() {
        level = new LevelBuilder(new BackgroundObjectBuilder(new AnimationCompiler())).buildLevel("/level.wkl", LEVEL_WIDTH, LEVEL_HEIGHT);
        return new Group(level.getLayerGroups().stream()
                .map(LayerGroup::getGroup)
                .collect(Collectors.toList()));
    }

    public void createHandlers(Group toolGroup, Group levelGroup) {
        levelGroup.setOnMouseClicked(e -> {
            selectedNode = level.getNodeMap().get(e.getTarget());
            String detailsHeading = "selected: " + selectedNode.getName();

            details.setText(detailsHeading);

            this.xBox.setText(Double.toString(selectedNode.getDataElement().getLocation().getX()));
this.yBox.setText(Double.toString(selectedNode.getDataElement().getLocation().getY()));
            
            this.widthBox.setText(Double.toString(selectedNode.getDataElement().getSize().getWidth()));
            this.heightBox.setText(Double.toString(selectedNode.getDataElement().getSize().getHeight()));

            System.out.println(detailsHeading);
        });
    }
}
