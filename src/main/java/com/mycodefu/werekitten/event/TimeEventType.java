package com.mycodefu.werekitten.event;

public enum TimeEventType implements Event {
    tick;

    @Override
    public String getName() {
        return toString();
    }
}
