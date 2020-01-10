package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class PlayerEvent implements PipelineEvent {
	private Event event;

	@Override
	public String getPipelineName() {
		return "player";
	}

	@Override
	public Event getEvent() {
		return event;
	}

	public PlayerEvent(PlayerEventType eventType) {
	event = eventType;
}

}
