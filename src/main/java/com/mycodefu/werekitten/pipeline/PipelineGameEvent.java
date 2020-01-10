package com.mycodefu.werekitten.pipeline;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;

public class PipelineGameEvent implements PipelineEvent{
private Event event;

@Override
public PipelineEventType getEventType() {
	return PipelineEventType.Game;
}

@Override
public Event getEvent() {
	return event;
}

public PipelineGameEvent(GameEventType eventType) {
	event = eventType;
}
}
