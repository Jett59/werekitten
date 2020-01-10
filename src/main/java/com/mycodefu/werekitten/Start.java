package com.mycodefu.werekitten;


import java.util.HashMap;
import java.util.Map;
import com.mycodefu.werekitten.pipeline.Pipeline;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.PipelineEventType;
import com.mycodefu.werekitten.pipeline.events.game.StartGameEvent;
import com.mycodefu.werekitten.pipeline.handlers.game.UICreationHandler;
import com.mycodefu.werekitten.pipeline.handlers.game.WindowCloseHandler;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application implements PipelineContext {
    private final Map<PipelineEventType,Pipeline> pipelines;
    private Stage stage;

    public Start() {
        this.pipelines = new HashMap<>();
        this.pipelines.put(PipelineEventType.Game, new Pipeline(PipelineEventType.Game, this, 1, new UICreationHandler(), new WindowCloseHandler()));
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
                pipelines.get(event.getEventType()).addEvent(event);
    }
}
