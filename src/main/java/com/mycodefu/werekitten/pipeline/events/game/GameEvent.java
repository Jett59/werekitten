package com.mycodefu.werekitten.pipeline.events.game;

<<<<<<< HEAD
=======
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
>>>>>>> wip: adding the player ability move
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class GameEvent implements PipelineEvent {
	@Override
	public String getPipelineName() {
		return "game";
	}
}
