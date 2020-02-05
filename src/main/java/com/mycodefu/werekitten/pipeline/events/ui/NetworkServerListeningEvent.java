package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkServerListeningEvent extends UiEvent {
    int port;

    public NetworkServerListeningEvent(int port) {
    	System.out.println(port);
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public Event getEvent() {
        return UiEventType.networkServerListening;
    }
}
