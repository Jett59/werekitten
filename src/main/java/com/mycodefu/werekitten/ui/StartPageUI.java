package com.mycodefu.werekitten.ui;

import javax.swing.GroupLayout.Alignment;

import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.events.game.BuildLevelEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkConnectClientEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class StartPageUI {
    public static String buttonStyle = "-fx-background-color: #666666; -fx-text-fill: #ccccff; ";
    private String preferredIp;

    public StartPageUI(String preferredIp) {
        this.preferredIp = preferredIp;
    }

    public Scene getScene(PipelineContext context){
        Text welcome = new Text("WELCOME TO WEREKITTEN");
        welcome.setFocusTraversable(true);
        welcome.setFont(new Font("Arial", 42));
        welcome.setTextAlignment(TextAlignment.CENTER);

        AnimationCompiler animationCompiler = new AnimationCompiler();
        Animation catAnimation = animationCompiler.compileAnimation("cat", "Idle", 10, Duration.seconds(1), false, 0.5);
        catAnimation.setCycleCount(javafx.animation.Animation.INDEFINITE);
        catAnimation.play();

        Font buttonFont = new Font("Arial", 24);

        Button singleplayer = new Button("Single Player");
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
        Button joinServer = new Button("Join LAN Server");
        joinServer.setFont(buttonFont);

        joinServer.setStyle(buttonStyle);

        final FlowPane imagePane = new FlowPane(catAnimation.getImageView());
        imagePane.setPrefWidth(230);

        joinServer.setOnAction(e->{
            Node serverNode = getJoinServerScreenNode(context, welcome, imagePane);
            Scene connectScene = new Scene(new Group(serverNode));
            connectScene.setFill(new Color(0.05, 0.2, 0.95, 0.25));
            context.getStage().setScene(connectScene);
        });
        Button settings = new Button("settings");
        settings.setFont(buttonFont);
        settings.setStyle(buttonStyle);
        settings.setAlignment(Alignment.CENTER);
        
        VBox buttons = new VBox(10, singleplayer, hostServer, joinServer, settings);
        buttons.setPadding(new Insets(50, 0, 0, 0));


        BorderPane border = new BorderPane();
        border.setTop(welcome);
        border.setLeft(imagePane);
        border.setRight(buttons);

        Group root = new Group(border);
        Scene scene = new Scene(root);
        scene.setFill(new Color(0.05, 0.2, 0.95, 0.25));

        return scene;
    }

    private BorderPane getJoinServerScreenNode(PipelineContext context, Text welcome, FlowPane imagePane) {
        BorderPane border = new BorderPane();
        border.setTop(welcome);
        border.setLeft(imagePane);

        Text addressText = new Text("address ->");
        TextField accessibleAddress = new TextField("put the lan address in the box");
        accessibleAddress.setTranslateX(-500000);
        accessibleAddress.setEditable(false);
        TextField address = new TextField(preferredIp != null && !preferredIp.equals("") ? preferredIp : "wss://echo.websocket.org");
        address.setStyle(buttonStyle);
        Button connect = new Button("connect");
        connect.setStyle(buttonStyle);

        connect.setOnAction(actionEvent -> {
            NetworkConnectClientEvent networkConnectClientEvent = new NetworkConnectClientEvent(address.getText());
            context.postEvent(networkConnectClientEvent);
        });

        HBox buttons = new HBox(10, accessibleAddress, addressText, address, connect);
        buttons.setPadding(new Insets(50, 0, 0, 0));
        VBox combinedAccessibilityAndButtons = new VBox(accessibleAddress, buttons);

        border.setRight(combinedAccessibilityAndButtons);

        return border;
    }
}
