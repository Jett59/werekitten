package com.mycodefu.werekitten.pipeline.events.network;

import com.mycodefu.werekitten.event.NetworkEventType;

public class NetworkConnectClientEvent extends NetworkEvent {
    private String serverAddress;

    public NetworkConnectClientEvent(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public NetworkEventType getNetworkEvent() {
        return NetworkEventType.connect;
    }

    @Override
    public String toString() {
        return "NetworkConnectClientEvent{" +
                "serverAddress='" + serverAddress + '\'' +
                '}';
    }
}
