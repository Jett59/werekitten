package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event; 
import com.mycodefu.werekitten.event.PlayerEventType;

public class StopMovingRightEvent extends PlayerEvent {

    public StopMovingRightEvent(String playerId) {
        super(playerId);
    }

    @Override
    public Event getEvent() {
        return PlayerEventType.stopMovingRight;
    }
}
