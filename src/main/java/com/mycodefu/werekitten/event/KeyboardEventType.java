package com.mycodefu.werekitten.event;

public enum KeyboardEventType implements Event {
    F3Pressed,
    F3Released,
    cPressed,
    cReleased,
    aPressed,
    aReleased,
    dPressed,
    dReleased,
    spacePressed,
    spaceReleased,
    registerListener,
    unregisterListener;

    @Override
    public String getName() {
        return toString();
    }
}
