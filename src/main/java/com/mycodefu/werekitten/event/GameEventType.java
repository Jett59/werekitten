package com.mycodefu.werekitten.event;

public enum GameEventType implements Event {
    start,
    buildLevel,
    levelLoaded,
    quit, levelDesigner;

    @Override
    public String getName() {
        return toString();
    }
}
