package com.mycodefu.werekitten.pipeline.handlers.game;

import java.awt.Toolkit;

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
                	Text welcome = new Text("WELCOME TO WEREKITTEN");
                	welcome.setFont(new Font(50));
                    ImageView imageView = new ImageView("/characters/cat/animations/idle/Idle (1).png");
                    imageView = new ImageView(SwingFXUtils.toFXImage(ImageHelper.scaleImage(SwingFXUtils.fromFXImage(imageView.getImage(), null), 0.5), null));
                    Button button = new Button("Play!");
                    button.setOnAction(actionEvent -> {
                        var screen = Toolkit.getDefaultToolkit().getScreenSize();
                        BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());
                        LevelBuilder levelBuilder = new LevelBuilder(backgroundObjectBuilder);
                        GameLevel level = levelBuilder.buildLevel("/level.wkl", screen.width, screen.height);
                        context.getStage().hide();

                        context.level().set(level);
                        context.postEvent(new LevelLoadedEvent());
                    });

                    FlowPane flowPane = new FlowPane(welcome, imageView, button);
                    Scene scene = new Scene(flowPane);
                    context.getStage().setTitle("werekitten launcher");
                    context.getStage().setScene(scene);
                    context.getStage().setWidth(640);
                    context.getStage().setHeight(480);
                    context.getStage().show();
                }
            }
        }
    }
}
