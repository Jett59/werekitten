package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkMoveLeftEvent extends UiEvent {
    String playerId;

    public NetworkMoveLeftEvent(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public Event getEvent() {
        return UiEventType.networkMoveLeft;
    }
}
