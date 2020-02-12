package com.mycodefu.werekitten.event;

/**
 * Used to start and stop the networking in the application.
 */
public enum NetworkEventType implements Event{
    start,
    stop, connect;

	@Override
	public String getName() {
		return toString();
	}

}
