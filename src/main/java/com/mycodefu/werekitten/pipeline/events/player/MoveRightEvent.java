package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;

public class MoveRightEvent extends PlayerEvent {
    private final double x;

    public MoveRightEvent(String playerId) {
        this(playerId, Double.NaN);
    }

    public MoveRightEvent(String playerId, double x) {
        super(playerId);
        this.x = x;
    }

    /**
     * The X position to sync to before starting movement.
     */
    public double getX() {
        return x;
    }

    @Override
    public Event getEvent() {
        return PlayerEventType.moveRight;
    }

    @Override
    public String toString() {
        return "MoveRightEvent{" +
                "x=" + x +
                '}';
    }
}
