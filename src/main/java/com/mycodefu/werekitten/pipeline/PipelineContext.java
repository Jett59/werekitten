package com.mycodefu.werekitten.pipeline;

public interface PipelineContext {
    void postEvent(PipelineEvent event);
}
