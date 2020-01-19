package com.mycodefu.werekitten.pipeline.events.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class GameEvent implements PipelineEvent {
	@Override
	public String getPipelineName() {
		return "game";
	}
}
