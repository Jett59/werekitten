package com.mycodefu.werekitten.event;

public enum KeyboardEventType implements Event{
leftPressed,
leftReleased,
rightPressed,
rightReleased,
spacePressed,
spaceReleased;

@Override
public String getName() {
	return toString();
}
}
