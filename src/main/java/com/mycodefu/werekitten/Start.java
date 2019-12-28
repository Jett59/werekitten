package com.mycodefu.werekitten;

import java.util.concurrent.atomic.AtomicReference;

import com.mycodefu.werekitten.application.GameLoop;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.keyboard.KeyType;
import com.mycodefu.werekitten.keyboard.KeyboardListener;
import com.mycodefu.werekitten.netty.server.NettyServer;
import com.mycodefu.werekitten.netty.server.NettyServerHandler.ServerConnectionCallback;
import com.mycodefu.werekitten.sound.MusicPlayer;
import com.mycodefu.werekitten.ui.GameUI;

import io.netty.channel.ChannelId;
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
    	final AtomicReference<GameLoop> gameLoop = new AtomicReference<>();
    	gameLoop.set(null);
    	final NettyServer server = new NettyServer(0, new ServerConnectionCallback() {
			
			@Override
			public void messageReceived(ChannelId id, String sourceIpAddress, String message) {
				System.out.println(String.format("recieved message from %s: %s", sourceIpAddress, message));
				PlayerEventType eventType;
				try {
				eventType = PlayerEventType.valueOf(message);
				gameLoop.get().addPlayerEvent(eventType);
				}catch(Exception e) {
					System.out.println(String.format("message %s is not a valid playEventType", message));
				}
			}
			
			@Override
			public void connectionReceived(ChannelId id) {
				System.out.println(String.format("recieved connection from %s", id.asLongText()));
			}
		});
        stage.show();
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setOnCloseRequest(e -> {
        	server.close();
            System.exit(0);
        });
        stage.setTitle("werekitten");

        GameUI gameUI = null;
        try {
            gameUI = new GameUI();
            Scene scene = gameUI.getScene(SCREEN_WIDTH, SCREEN_HEIGHT);
            stage.setScene(scene);
        } catch (Exception e) {
        	server.close();
            System.exit(1);
        }

        gameLoop.set(new GameLoop(gameUI));
        gameLoop.get().start();

        KeyboardListener keyboardListener = new KeyboardListener(event->{
        	gameLoop.get().addPlayerEvent(event);
        });

        keyboardListener.addKeyboardReleasedCallback(type->{
        	if(type.equals(KeyType.left)) {
        		gameLoop.get().addPlayerEvent(PlayerEventType.stopMovingLeft);
        	}
        	if(type.equals(KeyType.right)) {
        		gameLoop.get().addPlayerEvent(PlayerEventType.stopMovingRight);
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
        
        server.listen();
        
        System.out.println(server.getPort());
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
