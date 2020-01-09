package com.mycodefu.werekitten.pipeline.handlers.application;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.PipelineEventType;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class GameUIInitializerHandler implements PipelineHandler {
    @Override
    public PipelineEventType[] registerEventTypeInterest() {
        return new PipelineEventType[] {PipelineEventType.Application};
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {

    }
}
