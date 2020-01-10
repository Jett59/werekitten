package com.mycodefu.werekitten.pipeline;

import com.mycodefu.werekitten.event.Event;

public interface PipelineEvent {
    PipelineEventType getEventType();
    Event getEvent();
}
