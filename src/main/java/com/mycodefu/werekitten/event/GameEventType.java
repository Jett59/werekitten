package com.mycodefu.werekitten.event;

public enum GameEventType implements Event {
    start,
    buildLevel,
    levelLoaded,
    quit;

    @Override
    public String getName() {
        return toString();
    }
}
