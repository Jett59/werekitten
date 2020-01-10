package com.mycodefu.werekitten.pipeline.events.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.PipelineEventType;

public class StartGameEvent implements PipelineEvent {
    @Override
    public PipelineEventType getEventType() {
        return PipelineEventType.Game;
    }

    @Override
    public Event getEvent() {
        return GameEventType.start;
    }
}
