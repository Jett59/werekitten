package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.data.LayerType;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GameUI implements UI, UIConnectCallback {
    public static final double CAT_JUMP_AMOUNT = 100;

    private Group playerGroup = new Group();
    private List<UIEventCallback> callbacks = new ArrayList<>();
    private Scene scene;
    private Text address = new Text();

    public SceneLevel getScene(GameLevel level) {
        try {
            Group combinedGroup = new Group();

            for (LayerGroup layerGroup : level.getLayerGroups()) {
                if (layerGroup.getLayerType() == LayerType.Player) {
                    playerGroup.setLayoutY(level.getPlayerElement().getLocation().getY());
                    combinedGroup.getChildren().add(playerGroup);
                } else {
                    combinedGroup.getChildren().add(layerGroup.getGroup());
                }
            }
            address.setFont(new Font(24));
            combinedGroup.getChildren().add(address);


            Pane pane = new Pane();


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
    public void setAddress(String wsAddress) {
address.setText("Listening at: "+wsAddress);
address.setLayoutX(25);
address.setLayoutY(25);
        System.out.println("Listening at: " + wsAddress);
    }

    public void setIP(String localIPAddress) {
        
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
