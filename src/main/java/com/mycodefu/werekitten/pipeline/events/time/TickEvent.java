package com.mycodefu.werekitten.pipeline.events.time;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.TimeEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public class TickEvent implements PipelineEvent {
    @Override
    public String getPipelineName() {
        return "pipeline";
    }

    @Override
    public Event getEvent() {
        return TimeEventType.tick;
    }
}
