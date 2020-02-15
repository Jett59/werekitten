package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.image.ImageHelper;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.BuildLevelEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkConnectClientEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LoadLevelHandler implements PipelineHandler {
	public static String buttonStyle = "-fx-background-color: #666666; -fx-text-fill: #ccccff; ";
	
	BackgroundObjectBuilder backgroundObjectBuilder;

	public LoadLevelHandler() {
		backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());
	}
	
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("game")) {
            GameEventType gameEventType = (GameEventType)event.getEvent();
            switch (gameEventType) {
                case start: {
                	Text welcome = new Text("WELCOME TO WEREKITTEN");
                	welcome.setFont(new Font("Arial", 42));
                	welcome.setTextAlignment(TextAlignment.CENTER);

                    ImageView imageView = new ImageView("/characters/cat/animations/idle/Idle (1).png");
                    imageView = new ImageView(SwingFXUtils.toFXImage(ImageHelper.scaleImage(SwingFXUtils.fromFXImage(imageView.getImage(), null), 0.5), null));

                    Font buttonFont = new Font("Arial", 24);

                    Button singleplayer = new Button("Singleplayer");
                    singleplayer.setStyle(buttonStyle);
                    singleplayer.setFont(buttonFont);
                    singleplayer.setOnAction(actionEvent -> {
                        context.postEvent(new BuildLevelEvent(false));
                    });
                    Button hostServer = new Button("Host LAN Server");
                    hostServer.setStyle(buttonStyle);
                    hostServer.setFont(buttonFont);
                    hostServer.setOnAction(e->{
                        context.postEvent(new BuildLevelEvent(true));
                    });
                    Button joinServer = new Button("join lan server");
                    joinServer.setFont(buttonFont);

                    joinServer.setStyle(buttonStyle);


                    final FlowPane imagePane = new FlowPane(imageView);
                    imagePane.setPrefWidth(230);

                    joinServer.setOnAction(e->{
                    	Node serverNode = getJoinServerScreenNode(context, welcome, imagePane);
                        Scene connectScene = new Scene(new Group(serverNode));
                        connectScene.setFill(new Color(0.05, 0.2, 0.95, 0.25));
                        context.getStage().setScene(connectScene);
                    });
                    VBox buttons = new VBox(10, singleplayer, hostServer, joinServer);
                    buttons.setPadding(new Insets(50, 0, 0, 0));


                    BorderPane border = new BorderPane();
                    border.setTop(welcome);
                    border.setLeft(imagePane);
                    border.setRight(buttons);

                    Group root = new Group(border);
                    Scene scene = new Scene(root);
                    scene.setFill(new Color(0.05, 0.2, 0.95, 0.25));
                    context.getStage().setTitle("werekitten launcher");
                    context.getStage().setScene(scene);
                    context.getStage().setWidth(640);
                    context.getStage().setHeight(480);
                    context.getStage().show();
                }
            }
        }
    }
    
    private BorderPane getJoinServerScreenNode(PipelineContext context, Text welcome, FlowPane imagePane) {
        BorderPane border = new BorderPane();
        border.setTop(welcome);
        border.setLeft(imagePane);

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

        HBox buttons = new HBox(10, addressText, address, connect);
        buttons.setPadding(new Insets(50, 0, 0, 0));

        border.setRight(buttons);

    	return border;
    }
}
