package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.ui.StartPageUI;
import javafx.scene.Scene;

public class StartPageHandler implements PipelineHandler {
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("game")) {
            GameEventType gameEventType = (GameEventType)event.getEvent();
            switch (gameEventType) {
                case start: {
                    StartPageUI startPageUI = new StartPageUI();
                    Scene scene = startPageUI.getScene(context);

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
