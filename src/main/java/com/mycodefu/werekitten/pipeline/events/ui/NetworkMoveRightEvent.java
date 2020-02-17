package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkMoveRightEvent extends UiEvent {
    private String playerId;
    public double amount;

    public NetworkMoveRightEvent(String playerId, double amount) {
        this.playerId = playerId;
        this.amount = amount;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public Event getEvent() {
        return UiEventType.networkMoveRight;
    }
}
