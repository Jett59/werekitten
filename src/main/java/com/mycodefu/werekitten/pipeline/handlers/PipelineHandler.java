package com.mycodefu.werekitten.pipeline.handlers;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public interface PipelineHandler {
Event[] getEventInterest();

    void handleEvent(PipelineContext context, PipelineEvent event);
}
