package com.mycodefu.werekitten.event;

public enum GameEventType implements Event {
    start,
    levelLoaded,
    quit;

    @Override
    public String getName() {
        return toString();
    }
}
