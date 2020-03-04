package com.mycodefu.werekitten.pipeline.handlers.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.event.TimeEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.player.*;
import com.mycodefu.werekitten.pipeline.events.time.TickEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Player;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerMotionHandler implements PipelineHandler {
    private Map<String, PlayerEvent> playerMovementEventMap = new ConcurrentHashMap<>();

    @Override
    public Event[] getEventInterest() {
        return Event.combineEvents(PlayerEventType.values(), TimeEventType.tick);
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
                case moveLeft:
                case moveRight:
                case stopMovingLeft:
                case stopMovingRight: {
                    playerMovementEventMap.put(playerId, playerEvent);
                    break;
                }
            }
        } else if (event instanceof TickEvent) {
            Set<String> playersToMove = playerMovementEventMap.keySet();
            for (String playerId : playersToMove) {
                PlayerEvent playerMovementEvent = playerMovementEventMap.get(playerId);
                if (playerMovementEvent != null) {
                    switch (playerMovementEvent.getPlayerEvent()) {
                        case moveLeft: {
                            MoveLeftEvent moveEvent = (MoveLeftEvent) playerMovementEvent;
                            if (moveEvent.getMode().equals(MoveMode.MoveBy)) {
                                Player player = context.getPlayerMap().get(playerId);
                                if (player != null) {
                                    player.moveLeft(moveEvent.x);
                                }
                            }
                            break;
                        }
                        case moveRight: {
                            MoveRightEvent moveEvent = (MoveRightEvent) playerMovementEvent;
                            if (moveEvent.getMode().equals(MoveMode.MoveBy)) {
                                Player player = context.getPlayerMap().get(playerId);
                                if (player != null) {
                                    player.moveRight(moveEvent.x);
                                }
                            }
                            break;
                        }
                        case stopMovingLeft: {
                            context.getPlayerMap().get(playerId).stopMovingLeft();
                            playerMovementEventMap.remove(playerId);
                            break;
                        }
                        case stopMovingRight: {
                            context.getPlayerMap().get(playerId).stopMovingRight();
                            playerMovementEventMap.remove(playerId);
                            break;
                        }
                    }
                }
            }
        }
    }
}
