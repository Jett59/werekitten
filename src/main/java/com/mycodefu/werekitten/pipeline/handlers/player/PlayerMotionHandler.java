package com.mycodefu.werekitten.pipeline.handlers.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveLeftEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveMode;
import com.mycodefu.werekitten.pipeline.events.player.MoveRightEvent;
import com.mycodefu.werekitten.pipeline.events.player.PlayerEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class PlayerMotionHandler implements PipelineHandler{

	@Override
	public Event[] getEventInterest() {
		return PlayerEventType.values();
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event instanceof PlayerEvent) {
			switch ((PlayerEventType)event.getEvent()) {
			case jump: {
				context.getPlayerMap().get(((PlayerEvent)event).getPlayerId()).jump();
				break;
			}
			case moveLeft: {
				MoveLeftEvent moveEvent = (MoveLeftEvent)event;
				if(moveEvent.getMode().equals(MoveMode.MoveBy)) {
					context.getPlayerMap().get(((PlayerEvent)event).getPlayerId()).moveLeft(moveEvent.x);
				}
				break;
			}
			case moveRight: {
				MoveRightEvent moveEvent = (MoveRightEvent)event;
				if(moveEvent.getMode().equals(MoveMode.MoveBy)) {
					context.getPlayerMap().get(((PlayerEvent)event).getPlayerId()).moveRight(moveEvent.x);
				}
				break;
			}
			case stopMovingLeft: {
				context.getPlayerMap().get(((PlayerEvent)event).getPlayerId()).stopMovingLeft();
				break;
			}
			case stopMovingRight: {
				context.getPlayerMap().get(((PlayerEvent)event).getPlayerId()).stopMovingRight();
				break;
			}
			}
		}
	}

}
