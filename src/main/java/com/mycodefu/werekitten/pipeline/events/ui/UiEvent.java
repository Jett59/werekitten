package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class UiEvent implements PipelineEvent {
    @Override
    public String getPipelineName() {
        return "pipeline";
    }
}
