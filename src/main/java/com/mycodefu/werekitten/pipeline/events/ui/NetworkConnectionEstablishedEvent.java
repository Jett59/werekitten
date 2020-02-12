package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;

public class NetworkConnectionEstablishedEvent extends UiEvent {
    private ConnectionType connectionType;
    private String address;

    public NetworkConnectionEstablishedEvent(ConnectionType connectionType, String address) {
        this.connectionType = connectionType;
        this.address = address;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public Event getEvent() {
        return UiEventType.networkConnectionEstablished;
    }

    public static enum ConnectionType {
        server,
        client
    }

    @Override
    public String toString() {
        return "NetworkConnectionEstablishedEvent{" +
                "connectionType=" + connectionType +
                ", address='" + address + '\'' +
                '}';
    }
}
