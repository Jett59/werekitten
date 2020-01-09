package com.mycodefu.werekitten.pipeline;

public interface PipelineContext {
    void postEvent(PipelineEventType eventType, PipelineEvent event);
}
