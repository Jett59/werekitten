package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkMoveRightEvent extends UiEvent {
    String playerId;

    public NetworkMoveRightEvent(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public Event getEvent() {
        return UiEventType.networkMoveRight;
    }
}
