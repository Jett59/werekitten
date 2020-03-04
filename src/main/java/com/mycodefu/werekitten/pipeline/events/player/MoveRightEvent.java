package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;

public class MoveRightEvent extends PlayerMovementEvent {
    public MoveRightEvent(String playerId, double x, MoveMode mode) {
        super(playerId, x, mode);
    }

    @Override
    public Event getEvent() {
        return PlayerEventType.moveRight;
    }

}
