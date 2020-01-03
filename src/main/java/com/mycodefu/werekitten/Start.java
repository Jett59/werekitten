package com.mycodefu.werekitten;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicReference;

import com.mycodefu.werekitten.application.GameLoop;
import com.mycodefu.werekitten.event.NetworkEventType;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.keyboard.KeyType;
import com.mycodefu.werekitten.keyboard.KeyboardListener;
import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.NettyClientHandler;
import com.mycodefu.werekitten.netty.server.NettyServer;
import com.mycodefu.werekitten.netty.server.NettyServerHandler.ServerConnectionCallback;
import com.mycodefu.werekitten.sound.MusicPlayer;
import com.mycodefu.werekitten.ui.GameUI;

import com.mycodefu.werekitten.ui.UIEventCallback;
import io.netty.channel.ChannelId;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Start extends Application implements UIEventCallback, NettyClientHandler.SocketCallback {
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    NettyServer server;
    NettyClient client = null;
    final AtomicReference<GameLoop> gameLoop = new AtomicReference<>();

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
    	AtomicReference<ChannelId> channelId = new AtomicReference<>();
    	
        server = new NettyServer(0, new ServerConnectionCallback() {

            @Override
            public void serverConnectionMessage(ChannelId id, String sourceIpAddress, String message) {
                System.out.println(String.format("recieved message from %s: %s", sourceIpAddress, message));
                PlayerEventType eventType;
                try {
                    eventType = PlayerEventType.valueOf(message);
                    gameLoop.get().addPlayer2Event(eventType);
                } catch (Exception e) {
                    System.out.println(String.format("message %s is not a valid playEventType", message));
                }
            }

            @Override
            public void serverConnectionClosed(ChannelId id) {
                gameLoop.get().addNetworkEvent(NetworkEventType.disconnected);
            }
            
            @Override
            public void serverConnectionOpened(ChannelId id) {
            	channelId.set(id);
                System.out.println(String.format("recieved connection from %s", id.asLongText()));
                gameLoop.get().addNetworkEvent(NetworkEventType.connected);
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
            gameUI.addUIEventListener(this);

            Scene scene = gameUI.getScene(SCREEN_WIDTH, SCREEN_HEIGHT);
            stage.setScene(scene);
        } catch (Exception e) {
            server.close();
            System.exit(1);
        }

        gameLoop.set(new GameLoop(gameUI));
        gameLoop.get().start();

        KeyboardListener keyboardListener = new KeyboardListener(event -> {
            gameLoop.get().addPlayer1Event(event);
            if(channelId.get() != null) {
            	server.sendMessage(channelId.get(), event.name());
            }
            if(client != null) {
            	client.sendMessage(event.name());
            }
        });

        keyboardListener.addKeyboardReleasedCallback(type -> {
            if (type.equals(KeyType.left)) {
                gameLoop.get().addPlayer1Event(PlayerEventType.stopMovingLeft);
                if(channelId.get() != null) {
                    server.sendMessage(channelId.get(), type.name());
                }
                if(client != null) {
                    client.sendMessage(type.name());
                }
            }
            if (type.equals(KeyType.right)) {
                gameLoop.get().addPlayer1Event(PlayerEventType.stopMovingRight);
                if(channelId.get() != null) {
                    server.sendMessage(channelId.get(), type.name());
                }
                if(client != null) {
                    client.sendMessage(type.name());
                }
            }
        });

        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            keyboardListener.keyPressed(keyEvent.getCode());
        });

        stage.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            keyboardListener.keyReleased(keyEvent.getCode());
        });

        keyboardListener.startListening();

        MusicPlayer.setInLevel();
        MusicPlayer.playLevel();

        server.listen();

        gameUI.setPort(server.getPort());
        gameUI.setIP(getLocalIPAddress());

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void connect(String address) {
        if (client == null) {
            try {
                client = new NettyClient(address, this);
                client.connect();
            } catch (Exception e) {
                e.printStackTrace();
                client = null;
            }
        }
    }

    @Override
    public void disconnect() {
        if (client != null) {
            try {
                client.disconnect();
            } finally {
                client = null;
            }
        }
    }

    @Override
    public void clientDisconnected(String id) {
        gameLoop.get().addNetworkEvent(NetworkEventType.disconnected);
    }

    @Override
    public void clientConnected(String id) {
        gameLoop.get().addNetworkEvent(NetworkEventType.connected);
    }

    @Override
    public void clientMessageReceived(String id, String text) {
        System.out.println(String.format("recieved message from client connection %s: %s", id, text));
        PlayerEventType eventType;
        try {
            eventType = PlayerEventType.valueOf(text);
            gameLoop.get().addPlayer2Event(eventType);
        } catch (Exception e) {
            System.out.println(String.format("message %s is not a valid playEventType", text));
        }

    }

    @Override
    public void clientError(String id, Throwable e) {
        System.out.println(e.toString());
    }

    private static String getLocalIPAddress(){
        String ip = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet6Address) continue;
                    ip = addr.getHostAddress();

                    System.out.println(iface.getDisplayName() + " " + ip);
                }
            }
            if (ip==null){
                throw new SocketException("No interface found");
            }
            return ip;

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
