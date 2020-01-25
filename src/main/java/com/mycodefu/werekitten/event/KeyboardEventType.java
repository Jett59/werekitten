package com.mycodefu.werekitten.event;

public enum KeyboardEventType implements Event{
leftPressed,
leftReleased,
rightPressed,
rightReleased,
spacePressed,
spaceReleased,
registerListener,
unregisterListener
	;

@Override
public String getName() {
	return toString();
}
}
