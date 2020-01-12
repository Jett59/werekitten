package com.mycodefu.werekitten.pipeline;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;

public class PipelinePlayerEvent implements PipelineEvent{
private Event event;

	@Override
	public PipelineEventType getEventType() {
		return PipelineEventType.Player;
	}

	@Override
	public Event getEvent() {
		return event;
	}

public PipelinePlayerEvent(PlayerEventType eventType) {
	event = eventType;
}

}
