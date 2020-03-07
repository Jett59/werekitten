package com.mycodefu.werekitten.pipeline.events.network;

import com.mycodefu.werekitten.event.NetworkEventType;

public class NetworkStartEvent extends NetworkEvent {
    @Override
    public NetworkEventType getNetworkEvent() {
        return NetworkEventType.start;
    }
}
