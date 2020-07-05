package com.mycodefu.werekitten.pipeline.handlers.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.player.*;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class PlayerMotionHandler implements PipelineHandler {
    @Override
    public Event[] getEventInterest() {
        return Event.combineEvents(PlayerEventType.values());
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof PlayerEvent) {
            PlayerEvent playerEvent = (PlayerEvent) event;
            String playerId = playerEvent.getPlayerId();
            switch ((PlayerEventType) event.getEvent()) {
                case jump: {
                    context.getPlayerMap().get(playerId).jump();
                    break;
                }
                case moveLeft: {
                    double x = ((MoveLeftEvent) event).getX();
                    context.getPlayerMap().get(playerId).moveLeft(x);
                    break;
                }
                case moveRight: {
                    double x = ((MoveRightEvent) event).getX();
                    context.getPlayerMap().get(playerId).moveRight(x);
                    break;
                }
                case stopMovingLeft: {
                    double lastX = ((StopMovingLeftEvent) playerEvent).getLastX();
                    context.getPlayerMap().get(playerId).stopMovingLeft(lastX);
                    break;
                }
                case stopMovingRight: {
                    double lastX = ((StopMovingRightEvent) playerEvent).getLastX();
                    context.getPlayerMap().get(playerId).stopMovingRight(lastX);
                    break;
                }
            }
        }
    }
}
