package com.mycodefu.werekitten.event;

public enum KeyboardEventType implements Event{
	F3Pressed,
	F3Released,
aPressed,
aReleased,
dPressed,
dReleased,
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
