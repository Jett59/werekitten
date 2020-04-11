package com.mycodefu.werekitten.ui;

import java.util.concurrent.atomic.AtomicInteger;

import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.events.game.BuildLevelEvent;
import com.mycodefu.werekitten.pipeline.events.game.QuitGameEvent;
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
    double width = 250d;
    
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
        singleplayer.setPrefWidth(width);
        singleplayer.setOnAction(actionEvent -> {
            context.postEvent(new BuildLevelEvent(false));
        });
        Button hostServer = new Button("Host LAN Server");
        hostServer.setStyle(buttonStyle);
        hostServer.setFont(buttonFont);
        hostServer.setPrefWidth(width);
        hostServer.setOnAction(e->{
            context.postEvent(new BuildLevelEvent(true));
        });
        Button joinServer = new Button("Join LAN Server");
        joinServer.setFont(buttonFont);

        joinServer.setStyle(buttonStyle);

        joinServer.setPrefWidth(width);
        
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
        settings.setPrefWidth(width/2d-5d);
        
        settings.setOnAction(e->{
        	Node settingsNode = getSettingsScreenNode(context, welcome, imagePane);
        	Scene settingsScene = new Scene(new Group(settingsNode));
        	settingsScene.setFill(new Color(0.05, 0.2, 0.95, 0.25));
        	context.getStage().setScene(settingsScene);
        });
        Button quit = new Button("quit");
        quit.setStyle(buttonStyle);
        quit.setFont(buttonFont);
        quit.setPrefWidth(width/2d-5d);
        quit.setOnAction(e->context.postEvent(new QuitGameEvent()));
        
        HBox settingsAndQuit = new HBox(10, settings, quit);
        
        VBox buttons = new VBox(10, singleplayer, hostServer, joinServer, settingsAndQuit);
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

        Button back = new Button("back");
        back.setStyle(buttonStyle);
        
        back.setOnAction(e->{
        	context.getStage().setScene(getScene(context));
        });
        
        Text addressText = new Text("address ->");
        Text accessibleAddress = new Text("put the lan address in the box");
        accessibleAddress.setTranslateX(-500000);
        accessibleAddress.setFocusTraversable(true);
        
        TextField address = new TextField(preferredIp != null && !preferredIp.equals("") ? preferredIp : "wss://echo.websocket.org");
        address.setStyle(buttonStyle);
        Button connect = new Button("connect");
        connect.setStyle(buttonStyle);

        connect.setOnAction(actionEvent -> {
            NetworkConnectClientEvent networkConnectClientEvent = new NetworkConnectClientEvent(address.getText());
            context.postEvent(networkConnectClientEvent);
        });

        HBox buttons = new HBox(10, back, accessibleAddress, addressText, address, connect);
        buttons.setPadding(new Insets(50, 0, 0, 0));
        VBox combinedAccessibilityAndButtons = new VBox(accessibleAddress, buttons);

        border.setRight(combinedAccessibilityAndButtons);

        return border;
    }
    private BorderPane getSettingsScreenNode(PipelineContext context, Text welcome, FlowPane imagePane) {
    	BorderPane result = new BorderPane();
    	result.setTop(welcome);
    	result.setLeft(imagePane);
      
      HBox[] preferences = new HBox[context.getPreferences().keySet().size()];
      AtomicInteger index = new AtomicInteger();
      context.getPreferences().forEach((key, value)->{
    	  Text text = new Text(key);
    	  text.setFocusTraversable(true);
      preferences[index.get()] = new HBox(text, new TextField(value));
      index.addAndGet(1);
      });
      
      Button cancel = new Button("cancel");
      cancel.setStyle(buttonStyle);
      
      cancel.setOnAction(e->{
      context.getStage().setScene(getScene(context));
      });
      Button apply = new Button("apply");
      apply.setStyle(buttonStyle);
      
      apply.setOnAction(e->{
      applyPreferences(context, preferences);
      });
      Button applyAndClose = new Button("apply and close");
      applyAndClose.setStyle(buttonStyle);
      
      applyAndClose.setOnAction(e->{
      applyPreferences(context, preferences);
      context.getStage().setScene(getScene(context));
      });
      
      result.setRight(new VBox(new VBox(preferences), new HBox(cancel, apply, applyAndClose)));
      return result;
    }
    
    private void applyPreferences(PipelineContext context, HBox[] preferences) {
    	for(HBox box : preferences) {
    	      String key = ((Text)(box.getChildren().get(0))).getText();
    	      String value = ((TextField)(box.getChildren().get(1))).getText();
    	      context.getPreferences().put(key, value);
    	}
    }
}
