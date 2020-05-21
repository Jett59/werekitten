package com.mycodefu.werekitten.event;

public enum ChatEventType implements Event{
	send,
	sent,
	recieved;

	@Override
	public String getName() {
		return name();
	}

}
