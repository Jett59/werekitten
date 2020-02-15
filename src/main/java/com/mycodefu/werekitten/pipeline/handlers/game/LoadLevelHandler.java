package com.mycodefu.werekitten.pipeline.handlers.game;

import java.util.concurrent.atomic.AtomicReference;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.image.ImageHelper;
import com.mycodefu.werekitten.level.data.Color;
import com.mycodefu.werekitten.level.data.Element;
import com.mycodefu.werekitten.level.data.ElementType;
import com.mycodefu.werekitten.level.data.Location;
import com.mycodefu.werekitten.level.data.Size;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.BuildLevelEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkConnectClientEvent;
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
	public static String buttonStyle = "-fx-background-color: #666666; -fx-text-fill: #ccccff; ";
	
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
                	backgroundElement.setFillColor(new Color(0.05, 0.2, 0.95, 0.25));
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
                    singleplayer.setScaleX(1.5);
                    singleplayer.setScaleY(1.5);
                    singleplayer.setTranslateX(-50);
                    singleplayer.setStyle(buttonStyle);
                    singleplayer.setOnAction(actionEvent -> {
                        context.postEvent(new BuildLevelEvent(false));
                    });
                    Button hostServer = new Button("Host LAN Server");
                    hostServer.setScaleX(1.5);
                    hostServer.setScaleY(1.5);
                    hostServer.setTranslateX(0);
                    hostServer.setStyle(buttonStyle);
                    hostServer.setOnAction(e->{
                    	context.postEvent(new BuildLevelEvent(true));
                    });
                    Button joinServer = new Button("join lan server");
                    joinServer.setScaleX(1.5);
                    joinServer.setScaleY(1.5);
                    joinServer.setTranslateX(55);
                    joinServer.setStyle(buttonStyle);
                    joinServer.setOnAction(e->{
                    	FlowPane serverPane = getJoinServerPane(context);
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
    
    private FlowPane getJoinServerPane(PipelineContext context) {
    	FlowPane flowPane;
    	Text addressText = new Text("address ->");
    	TextField accessibleAddress = new TextField("put the lan address in the box");
    	accessibleAddress.setTranslateX(-500);
    	accessibleAddress.setEditable(false);
    	TextField address = new TextField();
    	address.setStyle(buttonStyle);
    	Button connect = new Button("connect");
    	connect.setStyle(buttonStyle);

    	connect.setOnAction(actionEvent -> {
            NetworkConnectClientEvent networkConnectClientEvent = new NetworkConnectClientEvent(address.getText());
            context.postEvent(networkConnectClientEvent);
        });
    	
    	flowPane = new FlowPane(addressText, accessibleAddress, address, connect);
    	return flowPane;
    }
}
