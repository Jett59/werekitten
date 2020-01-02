package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.LevelReader;
import com.mycodefu.werekitten.level.data.Level;
import com.mycodefu.werekitten.player.Kitten;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameUI implements UI, UIConnectCallback {
    public static final int CAT_HEIGHT = 100;
public static final int CAT_JUMP_AMOUNT = 300;
    
    Player player1;
    Player player2;
    TopBar topBar;
    private List<UIEventCallback> callbacks = new ArrayList<>();

    public Scene getScene(int screenWidth, int screenHeight) {
        try {
            BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());

player1 = Kitten.create(CAT_JUMP_AMOUNT, CAT_HEIGHT, Duration.seconds(1));
            
            List<NodeObject> possibleCollisions = new ArrayList<>();

            Level defaultLevel = LevelReader.read("/level.wkl");
            List<LayerGroup> layerGroups = defaultLevel.getLayers().stream()
                    .map(layer -> {
                        List<NodeObject> elements = layer.getElements().stream().map(backgroundElement -> {
                            NodeObject nodeObject = backgroundObjectBuilder.build(backgroundElement);
                            return nodeObject;
                        }).collect(Collectors.toList());

                        if (layer.getDepth() >= 0) {
                            possibleCollisions.addAll(elements);
                        }

                        Group group = new Group(elements.stream().map(NodeObject::getNode).collect(Collectors.toList()));
                        return new LayerGroup(layer.getName(), group, layer.getScrollSpeed(), layer.getDepth());
                    })
                    .sorted(Comparator.comparingInt(LayerGroup::getDepth))
                    .collect(Collectors.toList());

            Group combinedGroup = new Group();
            boolean addedCat = false;

            for (LayerGroup layerGroup : layerGroups) {
                if (layerGroup.getDepth() >= 0 && !addedCat) {
                    combinedGroup.getChildren().add(player1.getGroup());
                    addedCat = true;
                }

                combinedGroup.getChildren().add(layerGroup.getGroup());
            }

            if (!addedCat) {
                combinedGroup.getChildren().add(player1.getGroup());
            }

            Pane pane = new Pane();

            topBar = new TopBar(pane, this);
            combinedGroup.getChildren().add(topBar);

            pane.getChildren().add(combinedGroup);

            Scene s = new Scene(pane);
            return s;


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create game UI scene", e);
        }
    }

    @Override
    public void addUIEventListener(UIEventCallback callback) {
        this.callbacks.add(callback);
    }

    @Override
    public void updateConnectionState(boolean connected) {
        this.topBar.updateConnectionState(connected);
    }

    @Override
    public void connect(String address) {
        for (UIEventCallback callback : this.callbacks) {
            callback.connect(address);
        }
    }

    @Override
    public void disconnect() {
        for (UIEventCallback callback : this.callbacks) {
            callback.disconnect();
        }
    }

    public void setPort(int port) {
        this.topBar.setPort(port);
    }

	@Override
	public Player getPlayer1() {
		return player1;
	}

	@Override
	public Player getPlayer2() {
		return player2;
	}
}
