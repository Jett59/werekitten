package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;

public class StopMovingLeftEvent extends PlayerEvent {
    private final double lastX;

    public StopMovingLeftEvent(String playerId) {
        this(playerId, Double.NaN);
    }

    public StopMovingLeftEvent(String playerId, double lastX) {
        super(playerId);
        this.lastX = lastX;
    }

    public double getLastX() {
        return lastX;
    }

    @Override
    public Event getEvent() {
        return PlayerEventType.stopMovingLeft;
    }

    @Override
    public String toString() {
        return "StopMovingLeftEvent{" +
                "lastX=" + lastX +
                '}';
    }
}
