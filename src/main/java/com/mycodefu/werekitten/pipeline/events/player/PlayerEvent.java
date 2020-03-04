package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class PlayerEvent implements PipelineEvent {
private String playerId;

	@Override
	public String getPipelineName() {
		return "pipeline";
	}

PlayerEvent(String playerId){
	this.playerId = playerId;
}

public String getPlayerId() {
	return playerId;
}
}
