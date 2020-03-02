package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;

public class StopMovingRightEvent extends PlayerEvent {

    public StopMovingRightEvent(String playerId) {
        super(playerId);
    }

    @Override
    public Event getEvent() {
        return PlayerEventType.stopMovingRight;
    }
}
