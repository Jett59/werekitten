package com.mycodefu.werekitten.application;

import com.mycodefu.werekitten.sound.MusicPlayer;
import com.mycodefu.werekitten.ui.GameUI;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
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

        MusicPlayer.playLevel();
    }




}
