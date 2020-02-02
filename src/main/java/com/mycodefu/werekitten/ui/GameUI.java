package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.level.data.LayerType;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameUI implements UI, UIConnectCallback {
    public static final double CAT_JUMP_AMOUNT = 100;

    private Group playerGroup = new Group();
    private TopBar topBar;
    private List<UIEventCallback> callbacks = new ArrayList<>();
    private Scene scene;

    public SceneLevel getScene(int screenWidth, int screenHeight) {
        try {
            BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());

            LevelBuilder levelBuilder = new LevelBuilder(backgroundObjectBuilder);
            GameLevel level = levelBuilder.buildLevel("/level.wkl", screenWidth, screenHeight);
            List<LayerGroup> layerGroups = level.getLayerGroups();

            Group combinedGroup = new Group();

            for (LayerGroup layerGroup : layerGroups) {
                if (layerGroup.getLayerType() == LayerType.Player) {
                    playerGroup.setLayoutY(level.getPlayerElement().getLocation().getY());
                    combinedGroup.getChildren().add(playerGroup);
                } else {
                    combinedGroup.getChildren().add(layerGroup.getGroup());
                }
            }


            Pane pane = new Pane();

            topBar = new TopBar(pane, this);
            combinedGroup.getChildren().add(topBar);

            pane.getChildren().add(combinedGroup);

            scene = new Scene(pane);
            return new SceneLevel(scene, level);


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

    @Override
    public void setPort(int port) {
        this.topBar.setPort(port);
    }

    public void setIP(String localIPAddress) {
        this.topBar.setLocalIPAddress(localIPAddress);
    }

    @Override
    public void addPlayer(Player player) {
        playerGroup.getChildren().add(player.getGroup());
        System.out.println("added player group to scene");
    }

    @Override
    public void removePlayer(Player player) {
        playerGroup.getChildren().remove(player.getGroup());
    }

}
