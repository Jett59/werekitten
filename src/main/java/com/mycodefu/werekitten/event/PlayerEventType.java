package com.mycodefu.werekitten.event;

public enum PlayerEventType implements Event{
    moveLeft,
    moveRight,
    jump,
    stopMovingLeft,
    stopMovingRight;

	@Override
	public String getName() {
		return toString();
	}
}
