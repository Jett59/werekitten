package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkMoveLeftEvent extends UiEvent {
    private String playerId;
    public double amount;

    public NetworkMoveLeftEvent(String playerId, double amount) {
        this.playerId = playerId;
        this.amount = amount;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public Event getEvent() {
        return UiEventType.networkMoveLeft;
    }
}
