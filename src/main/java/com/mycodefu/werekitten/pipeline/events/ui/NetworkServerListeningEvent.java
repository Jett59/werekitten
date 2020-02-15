package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkServerListeningEvent extends UiEvent {
    private String address;

    public NetworkServerListeningEvent(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public Event getEvent() {
        return UiEventType.networkServerListening;
    }
}
