package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event; 
import com.mycodefu.werekitten.event.PlayerEventType;

public class StopMovingRightEvent extends PlayerEvent {
    private final double lastX;

    public StopMovingRightEvent(String playerId) {
        this(playerId, 0);
    }

    public StopMovingRightEvent(String playerId, double lastX) {
        super(playerId);
        this.lastX = lastX;
    }

    public double getLastX() {
        return lastX;
    }

    @Override
    public Event getEvent() {
        return PlayerEventType.stopMovingRight;
    }

    @Override
    public String toString() {
        return "StopMovingRightEvent{" +
                "lastX=" + lastX +
                '}';
    }
}
