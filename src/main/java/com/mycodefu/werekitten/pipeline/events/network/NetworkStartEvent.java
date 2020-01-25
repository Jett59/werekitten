package com.mycodefu.werekitten.pipeline.events.network;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.NetworkEventType;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;

public class NetworkStartEvent extends NetworkEvent {
    @Override
    public Event getEvent() {
        return NetworkEventType.start;
    }
}
