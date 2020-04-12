package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;

public class MoveLeftEvent extends PlayerEvent {
    private final double x;

    public MoveLeftEvent(String playerId, double x) {
        super(playerId);
        this.x = x;
    }

    /**
     * The X position to sync to before starting movement.
     */
    public double getX() {
        return x;
    }

    public Event getEvent() {
        return PlayerEventType.moveLeft;
    }

}
