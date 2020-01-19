package com.mycodefu.werekitten.pipeline.events.game;

import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class GameEvent implements PipelineEvent {
	@Override
	public String getPipelineName() {
		return "game";
	}
}
