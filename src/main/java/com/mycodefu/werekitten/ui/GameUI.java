package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.level.LevelReader;
import com.mycodefu.werekitten.level.data.Element;
import com.mycodefu.werekitten.level.data.LayerType;
import com.mycodefu.werekitten.level.data.Level;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.slide.LayerGroup;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameUI implements UI, UIConnectCallback {
    public static final int CAT_HEIGHT = 100;
    public static final int CAT_JUMP_AMOUNT = 100;

    private Group playerGroup = new Group();
    private TopBar topBar;
    private List<UIEventCallback> callbacks = new ArrayList<>();
    private Scene scene;
    private Element playerElement = null;

    public Scene getScene(int screenWidth, int screenHeight) {
        try {
            BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());


            LevelBuilder levelBuilder = new LevelBuilder(backgroundObjectBuilder);
            List<LayerGroup> layerGroups = levelBuilder.buildLevel("/level.wkl");

            Group combinedGroup = new Group();

            for (LayerGroup layerGroup : layerGroups) {
            	if(layerGroup.getLayerType() == LayerType.Player) {
            		playerElement = layerGroup.getElements().get(0);
            		playerGroup.setLayoutY(playerElement.getLocation().getY());
            		combinedGroup.getChildren().add(playerGroup);
            	}else {
            		combinedGroup.getChildren().add(layerGroup.getGroup());
            	}
            	}


            Pane pane = new Pane();

            topBar = new TopBar(pane, this);
            combinedGroup.getChildren().add(topBar);

            pane.getChildren().add(combinedGroup);

            scene = new Scene(pane);
            return scene;


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

	public Element getPlayerElement() {
		return playerElement;
	}

	public void setPlayerElement(Element playerElement) {
		this.playerElement = playerElement;
	}
}
