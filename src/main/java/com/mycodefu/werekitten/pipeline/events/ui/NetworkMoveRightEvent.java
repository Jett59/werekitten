package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkMoveRightEvent extends UiEvent {
    private String playerId;
    public double x;

    public NetworkMoveRightEvent(String playerId, double x) {
        this.playerId = playerId;
        this.x=x;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public Event getEvent() {
        return UiEventType.networkMoveRight;
    }
    
    @Override
    public String toString() {
    	return "NetworkMoveRight " + x;
    }
}
