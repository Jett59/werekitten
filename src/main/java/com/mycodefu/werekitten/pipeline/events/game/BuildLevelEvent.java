package com.mycodefu.werekitten.pipeline.events.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;

public class BuildLevelEvent extends GameEvent{
	private boolean shouldHost;

	@Override
	public Event getEvent() {
		return GameEventType.buildLevel;
	}

public boolean shouldHost() {
	return shouldHost;
}

	public BuildLevelEvent(boolean shouldHost) {
		this.shouldHost = shouldHost;
	}
	
	public String toString() {
		return "BuildLevelEvent{shouldHost:"+shouldHost+"}";
	}
}
