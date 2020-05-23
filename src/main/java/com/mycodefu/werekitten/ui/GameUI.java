package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.data.LayerType;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GameUI implements UI, UIConnectCallback {
    public static final double CAT_JUMP_AMOUNT = 150;

    private Group playerGroup = new Group();
    private List<UIEventCallback> callbacks = new ArrayList<>();
    private Scene scene;
    private VBox addressBox;
    private TextField address = new TextField();
    private Group otherObjects = new Group();

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
            combinedGroup.setFocusTraversable(true);
            address.setFont(new Font("Arial", 24));
            address.setEditable(false);
            address.setBorder(Border.EMPTY);
            address.setBackground(Background.EMPTY);
            address.setStyle("-fx-text-fill: #fff;");
            address.setFocusTraversable(true);
            address.setPrefColumnCount(30);

            Text listeningAt = new Text("Listening at: ");
            listeningAt.setFont(new Font("Arial", 24));
            listeningAt.setFocusTraversable(true);
            listeningAt.setFill(Color.WHITE);

            addressBox = new VBox(5, listeningAt, address);
            addressBox.setVisible(false);
            
            Pane pane = new Pane();
            pane.getChildren().add(combinedGroup);
            pane.getChildren().add(addressBox);
            pane.getChildren().add(otherObjects);

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
        address.setText(wsAddress);
        address.setLayoutX(25);
        address.setLayoutY(25);
        System.out.println("Listening at: " + wsAddress);

        addressBox.setVisible(true);
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

    @Override
    public void addNode(Node n) {
    	otherObjects.getChildren().add(n);
    }
    
}
