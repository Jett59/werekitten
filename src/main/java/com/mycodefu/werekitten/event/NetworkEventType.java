package com.mycodefu.werekitten.event;

public enum NetworkEventType implements Event{
    connected,
    disconnected;

	@Override
	public String getName() {
		return toString();
	}

}
