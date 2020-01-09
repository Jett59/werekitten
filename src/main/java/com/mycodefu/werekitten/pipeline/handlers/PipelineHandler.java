package com.mycodefu.werekitten.pipeline.handlers;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.PipelineEventType;

public interface PipelineHandler {
    PipelineEventType[] registerEventTypeInterest();
    void handleEvent(PipelineContext context, PipelineEvent event);
}
