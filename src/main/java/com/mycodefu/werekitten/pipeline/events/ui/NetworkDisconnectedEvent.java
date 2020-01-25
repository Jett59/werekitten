package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkDisconnectedEvent extends UiEvent {
    @Override
    public Event getEvent() {
        return UiEventType.networkDisconnected;
    }
}
