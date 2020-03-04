package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;

public class MoveLeftEvent extends PlayerMovementEvent {

    public MoveLeftEvent(String playerId, double x, MoveMode mode) {
        super(playerId, x, mode);
    }

    @Override
    public Event getEvent() {
        return PlayerEventType.moveLeft;
    }

}
