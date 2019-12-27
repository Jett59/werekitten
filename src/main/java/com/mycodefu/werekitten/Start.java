package com.mycodefu.werekitten;

import com.mycodefu.werekitten.application.GameLoop;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.sound.MusicPlayer;
import com.mycodefu.werekitten.ui.GameUI;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;

    @Override
    public void start(Stage stage) {
        stage.show();
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        stage.setTitle("werekitten");

        GameUI gameUI = null;
        try {
            gameUI = new GameUI();
            Scene scene = gameUI.getScene(SCREEN_WIDTH, SCREEN_HEIGHT);
            stage.setScene(scene);
        } catch (Exception e) {
            System.exit(1);
        }

        GameLoop gameLoop = new GameLoop(gameUI);
        gameLoop.start();

        MusicPlayer.setInLevel();
        MusicPlayer.playLevel();


        AnimationTimer testTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameLoop.addPlayerEvent(PlayerEventType.moveRight);
            }
        };
        testTimer.start();
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
