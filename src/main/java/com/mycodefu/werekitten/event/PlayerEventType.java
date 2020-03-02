package com.mycodefu.werekitten.event;

public enum PlayerEventType implements Event{
	jump,
	moveLeft,
	moveRight,
	stopMovingLeft,
	stopMovingRight;

	@Override
	public String getName() {
		return name();
	}

}
