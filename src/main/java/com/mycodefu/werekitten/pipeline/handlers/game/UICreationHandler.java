package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class UICreationHandler implements PipelineHandler {
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        System.out.println("Starting game!!");
    }
}