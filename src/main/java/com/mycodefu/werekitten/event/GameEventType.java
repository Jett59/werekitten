package com.mycodefu.werekitten.event;

public enum GameEventType implements Event {
    start,
    buildLevel,
    levelLoaded,
    enableKeyboardListener,
    quit;

    @Override
    public String getName() {
        return toString();
    }
}
