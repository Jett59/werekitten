package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;

public class MoveRightEvent extends PlayerEvent {
    public double x;
    private MoveMode mode;

    public MoveRightEvent(String playerId, double x, MoveMode mode) {
        super(playerId);
        this.x=x;
        this.mode = mode;
    }

    @Override
    public Event getEvent() {
        return PlayerEventType.moveRight;
    }
    
    @Override
    public String toString() {
        return String.format("moveRight %s %f",  mode == MoveMode.MoveTo ? "to" : "by", x);
    }

    public MoveMode getMode() {
        return mode;
    }
}
