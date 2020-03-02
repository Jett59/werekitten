package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;

public class NetworkMoveRightEvent extends UiEvent {
    private String playerId;
    public double x;
    private NetworkMoveMode mode;

    public NetworkMoveRightEvent(String playerId, double x, NetworkMoveMode mode) {
        this.playerId = playerId;
        this.x=x;
        this.mode = mode;
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
        return String.format("NetworkMoveRight %s %f",  mode == NetworkMoveMode.MoveTo ? "to" : "by", x);
    }

    public NetworkMoveMode getMode() {
        return mode;
    }
}
