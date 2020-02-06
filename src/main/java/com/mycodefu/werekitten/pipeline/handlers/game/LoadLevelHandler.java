package com.mycodefu.werekitten.pipeline.handlers.game;

import java.awt.Toolkit;
import java.util.concurrent.atomic.AtomicReference;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.image.ImageHelper;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.level.data.Color;
import com.mycodefu.werekitten.level.data.Element;
import com.mycodefu.werekitten.level.data.ElementType;
import com.mycodefu.werekitten.level.data.Location;
import com.mycodefu.werekitten.level.data.Size;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.LevelLoadedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LoadLevelHandler implements PipelineHandler {
	public static String buttonStyle = "-fx-background-color: #000000; -fx-text-fill: #05ed1b; ";
	
	BackgroundObjectBuilder backgroundObjectBuilder;
	Element backgroundElement = new Element();
	
	public LoadLevelHandler() {
		backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());
	}
	
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("game")) {
            GameEventType gameEventType = (GameEventType)event.getEvent();
            switch (gameEventType) {
                case start: {
                	backgroundElement.setFillColor(new Color(0.05, 0.2, 0.81, 1));
                	backgroundElement.setType(ElementType.Rectangle);
                	backgroundElement.setLocation(new Location(0, 0));
                	backgroundElement.setSize(new Size(640, 480));
                	AtomicReference<FlowPane> flowPane = new AtomicReference<>();
                	Node backgroundNode = backgroundObjectBuilder.build(backgroundElement).getNode();
                	Text welcome = new Text("WELCOME TO WEREKITTEN");
                	welcome.setFont(new Font(50));
                    ImageView imageView = new ImageView("/characters/cat/animations/idle/Idle (1).png");
                    imageView = new ImageView(SwingFXUtils.toFXImage(ImageHelper.scaleImage(SwingFXUtils.fromFXImage(imageView.getImage(), null), 0.5), null));
                    Button singleplayer = new Button("Singleplayer");
                    singleplayer.setStyle(buttonStyle);
                    singleplayer.setOnAction(actionEvent -> {
                        buildLevel(context, false);
                    });
                    Button hostServer = new Button("host lan server");
                    hostServer.setStyle(buttonStyle);
                    hostServer.setOnAction(e->{
                    	buildLevel(context, true);
                    });
                    Button joinServer = new Button("join lan server");
                    joinServer.setStyle(buttonStyle);
                    joinServer.setOnAction(e->{
                    	FlowPane serverPane = getJoinServerPane();
                    	context.getStage().setScene(new Scene(new Group(backgroundNode, serverPane)));
                    });

                    flowPane.set(new FlowPane(welcome, imageView, singleplayer, hostServer, joinServer));
                    Scene scene = new Scene(new Group(backgroundNode, flowPane.get()));
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
    	address.setStyle(buttonStyle);
    	Button connect = new Button("connect");
    	connect.setStyle(buttonStyle);
    	
    	flowPane = new FlowPane(addressText, accessibleAddress, address, connect);
    	return flowPane;
    }
}
