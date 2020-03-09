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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerMotionHandler implements PipelineHandler {
    private Map<String, Map<PlayerEventType, PlayerEvent>> playerMovementEventMap = new ConcurrentHashMap<>();

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
                case moveRight: {
                    PlayerMovementEvent moveEvent = (PlayerMovementEvent) playerEvent;
                    if (moveEvent.getMode() == MoveMode.MoveTo) {
                        Player player = context.getPlayerMap().get(playerId);
                        switch (moveEvent.getPlayerEvent()) {
                            case moveLeft:
                                player.moveLeftTo(moveEvent.x);
                                break;
                            case moveRight:
                                player.moveRightTo(moveEvent.x);
                                break;
                        }
                    } else {
                        Map<PlayerEventType, PlayerEvent> eventMap = new HashMap<>();
                        eventMap.put(playerEvent.getPlayerEvent(), playerEvent);
                        playerMovementEventMap.put(playerId, eventMap);
                    }
                    break;
                }
                case stopMovingLeft: {
                    double lastX = ((StopMovingLeftEvent) playerEvent).getLastX();
                    if (lastX != 0d) {
                        context.getPlayerMap().get(playerId).moveTo(lastX);
                    }
                    playerMovementEventMap.get(playerId).put(playerEvent.getPlayerEvent(), playerEvent);
                    break;
                }
                case stopMovingRight: {
                    double lastX = ((StopMovingRightEvent) playerEvent).getLastX();
                    if (lastX != 0d) {
                        context.getPlayerMap().get(playerId).moveTo(lastX);
                    }
                    playerMovementEventMap.get(playerId).put(playerEvent.getPlayerEvent(), playerEvent);
                    break;
                }
            }
        } else if (event instanceof TickEvent) {
            Set<String> playersToMove = playerMovementEventMap.keySet();
            for (String playerId : playersToMove) {
                Set<PlayerEventType> playerEventTypes = new HashSet(playerMovementEventMap.get(playerId).keySet());
                for (PlayerEventType movementEventType : playerEventTypes) {
                    PlayerEvent playerMovementEvent = playerMovementEventMap.get(playerId).get(movementEventType);
                    if (playerMovementEvent != null) {
                        switch (movementEventType) {
                            case moveLeft: {
                                MoveLeftEvent moveEvent = (MoveLeftEvent) playerMovementEvent;
                                Player player = context.getPlayerMap().get(playerId);
                                if (player != null) {
                                    if (moveEvent.getMode() == MoveMode.MoveBy) {
                                        if (canMoveLeft(player, moveEvent.x, context)) {
                                            player.moveLeft(moveEvent.x);
                                        }
                                    }
                                }
                                break;
                            }
                            case moveRight: {
                                MoveRightEvent moveEvent = (MoveRightEvent) playerMovementEvent;
                                Player player = context.getPlayerMap().get(playerId);
                                if (player != null) {
                                    if (moveEvent.getMode() == MoveMode.MoveBy) {
                                        if (canMoveRight(player, moveEvent.x, context)) {
                                            player.moveRight(moveEvent.x);
                                        }
                                    }
                                }
                                break;
                            }
                            case stopMovingLeft: {
                                context.getPlayerMap().get(playerId).stopMovingLeft();
                                playerMovementEventMap.get(playerId).remove(movementEventType);
                                playerMovementEventMap.get(playerId).remove(PlayerEventType.moveLeft);
                                break;
                            }
                            case stopMovingRight: {
                                context.getPlayerMap().get(playerId).stopMovingRight();
                                playerMovementEventMap.get(playerId).remove(movementEventType);
                                playerMovementEventMap.get(playerId).remove(PlayerEventType.moveRight);
                                break;
                            }

                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private boolean canMoveLeft(Player player, double x, PipelineContext context) {
        return canMoveRight(player, -x, context);
    }

    private boolean canMoveRight(Player player, double x, PipelineContext context) {
        Polygon playerShape = player.getCurrentAnimation().getCurrentShape();
        playerShape.setLayoutX(player.getGroup().getLayoutX() + x);
        for (Player otherPlayer : context.getPlayerMap().values()) {
            if (player != otherPlayer) {
                Polygon otherPlayerShape = otherPlayer.getCurrentAnimation().getCurrentShape();
                otherPlayerShape.setLayoutX(otherPlayer.getGroup().getLayoutX());
                if (!Shape.intersect(playerShape, otherPlayerShape).getBoundsInLocal().isEmpty()) {
                    otherPlayer.moveTo(x * 5);
                    otherPlayer.dealDamage(5);
                    return false;
                }
            }
        }
        return true;
    }
}
