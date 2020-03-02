package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;

public class MoveLeftEvent extends PlayerEvent {
    public double x;
    private MoveMode mode;

    public MoveLeftEvent(String playerId, double x, MoveMode mode) {
        super(playerId);
        this.x = x;
        this.mode = mode;
    }

    @Override
    public Event getEvent() {
        return PlayerEventType.moveLeft;
    }
    @Override
    public String toString() {
    	return String.format("NetworkMoveLeft %s %f",  mode == MoveMode.MoveTo ? "to" : "by", x);
    }

    public MoveMode getMode() {
        return mode;
    }

}
