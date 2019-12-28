package com.mycodefu.werekitten;

import com.mycodefu.werekitten.application.GameLoop;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.keyboard.KeyType;
import com.mycodefu.werekitten.keyboard.KeyboardListener;
import com.mycodefu.werekitten.sound.MusicPlayer;
import com.mycodefu.werekitten.ui.GameUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Start extends Application {
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;

    @SuppressWarnings("exports")
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

        KeyboardListener keyboardListener = new KeyboardListener(event->{
        	gameLoop.addPlayerEvent(event);
        });

        keyboardListener.addKeyboardReleasedCallback(type->{
        	if(type.equals(KeyType.left)) {
        		gameLoop.addPlayerEvent(PlayerEventType.stopMovingLeft);
        	}
        	if(type.equals(KeyType.right)) {
        		gameLoop.addPlayerEvent(PlayerEventType.stopMovingRight);
        	}
        });
        
        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent->{
        	keyboardListener.keyPressed(keyEvent.getCode());
        });
        
        stage.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent->{
        	keyboardListener.keyReleased(keyEvent.getCode());
        });
        
        keyboardListener.startListening();
        
        MusicPlayer.setInLevel();
        MusicPlayer.playLevel();
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
