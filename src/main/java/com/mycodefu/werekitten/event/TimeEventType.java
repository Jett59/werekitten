package com.mycodefu.werekitten.event;

public enum TimeEventType implements Event {
    tick,
    framerate;

    @Override
    public String getName() {
        return toString();
    }
}
