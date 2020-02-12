package com.mycodefu.werekitten.pipeline.events.network;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.NetworkEventType;

public class NetworkStopEvent extends NetworkEvent {
    @Override
    public NetworkEventType getNetworkEvent() {
        return NetworkEventType.stop;
    }
}
