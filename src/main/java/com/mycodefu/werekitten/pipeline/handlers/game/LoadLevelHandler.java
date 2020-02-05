package com.mycodefu.werekitten.pipeline.handlers.game;

import java.awt.Toolkit;
import java.util.concurrent.atomic.AtomicReference;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.image.ImageHelper;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.LevelLoadedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LoadLevelHandler implements PipelineHandler {
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("game")) {
            GameEventType gameEventType = (GameEventType)event.getEvent();
            switch (gameEventType) {
                case start: {
                	AtomicReference<FlowPane> flowPane = new AtomicReference<>();
                	Text welcome = new Text("WELCOME TO WEREKITTEN");
                	welcome.setFont(new Font(50));
                    ImageView imageView = new ImageView("/characters/cat/animations/idle/Idle (1).png");
                    imageView = new ImageView(SwingFXUtils.toFXImage(ImageHelper.scaleImage(SwingFXUtils.fromFXImage(imageView.getImage(), null), 0.5), null));
                    Button singleplayer = new Button("Singleplayer");
                    singleplayer.setOnAction(actionEvent -> {
                        buildLevel(context, false);
                    });
                    Button hostServer = new Button("host lan server");
                    hostServer.setOnAction(e->{
                    	buildLevel(context, true);
                    });
                    Button joinServer = new Button("join lan server");
                    joinServer.setOnAction(e->{
                    	FlowPane serverPane = getJoinServerPane();
                    	context.getStage().setScene(new Scene(serverPane));
                    });

                    flowPane.set(new FlowPane(welcome, imageView, singleplayer, hostServer, joinServer));
                    Scene scene = new Scene(flowPane.get());
                    context.getStage().setTitle("werekitten launcher");
                    context.getStage().setScene(scene);
                    context.getStage().setWidth(640);
                    context.getStage().setHeight(480);
                    context.getStage().show();
                }
            }
        }
    }
    
    private void buildLevel(PipelineContext context, boolean shouldListenOnLan) {
    	var screen = Toolkit.getDefaultToolkit().getScreenSize();
        BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());
        LevelBuilder levelBuilder = new LevelBuilder(backgroundObjectBuilder);
        GameLevel level = levelBuilder.buildLevel("/level.wkl", screen.width, screen.height);
        context.getStage().hide();

        context.level().set(level);
        context.postEvent(new LevelLoadedEvent(shouldListenOnLan));
    }
    
    private FlowPane getJoinServerPane() {
    	FlowPane flowPane;
    	Text addressText = new Text("address ->");
    	TextField accessibleAddress = new TextField("put the lan address in the box");
    	accessibleAddress.setTranslateX(-500);
    	accessibleAddress.setEditable(false);
    	TextField address = new TextField();
    	Button connect = new Button("connect");
    	
    	flowPane = new FlowPane(addressText, accessibleAddress, address, connect);
    	return flowPane;
    }
}
