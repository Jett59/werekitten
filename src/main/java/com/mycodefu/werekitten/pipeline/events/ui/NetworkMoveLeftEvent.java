package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkMoveLeftEvent extends UiEvent {
    private String playerId;
    public double x;

    public NetworkMoveLeftEvent(String playerId, double x) {
        this.playerId = playerId;
        this.x = x;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public Event getEvent() {
        return UiEventType.networkMoveLeft;
    }
    @Override
    public String toString() {
    	return "NetworkMoveLeft " + x;
    }
}
