package com.mycodefu.werekitten;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.Pipe;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
import com.mycodefu.werekitten.pipeline.Pipeline;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.PipelineEventType;
import com.mycodefu.werekitten.pipeline.events.game.StartGameEvent;
import com.mycodefu.werekitten.pipeline.handlers.game.UICreationHandler;
import com.mycodefu.werekitten.sound.MusicPlayer;
import com.mycodefu.werekitten.ui.GameUI;

import com.mycodefu.werekitten.ui.UIEventCallback;
import io.netty.channel.ChannelId;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Start extends Application implements PipelineContext {
    private final Map<PipelineEventType,Pipeline> pipelines;
    private Stage stage;

    public Start() {
        this.pipelines = new HashMap<>();
        this.pipelines.put(PipelineEventType.Game, new Pipeline(PipelineEventType.Game, this, 1, new UICreationHandler()));
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.postEvent(new StartGameEvent());

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                for (Pipeline pipeline : pipelines.values()) {
                    pipeline.processEvents();
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public void postEvent(PipelineEvent event) {
        switch (event.getEventType()) {
            case Game: {
                pipelines.get(PipelineEventType.Game).addEvent(event);
                break;
            }
        }
    }
}
