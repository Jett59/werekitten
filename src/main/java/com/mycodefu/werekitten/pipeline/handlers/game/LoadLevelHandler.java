package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.LevelLoadedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.awt.*;

public class LoadLevelHandler implements PipelineHandler {
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("game")) {
            GameEventType gameEventType = (GameEventType)event.getEvent();
            switch (gameEventType) {
                case start: {
                    ImageView imageView = new ImageView("/characters/cat/animations/idle/Idle (1).png");
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

                    FlowPane flowPane = new FlowPane(imageView, button);
                    Scene scene = new Scene(flowPane);
                    context.getStage().setScene(scene);
                    context.getStage().setWidth(640);
                    context.getStage().setHeight(480);
                    context.getStage().show();
                }
            }
        }
    }
}
