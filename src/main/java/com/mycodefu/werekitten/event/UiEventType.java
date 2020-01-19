package com.mycodefu.werekitten.event;

public enum UiEventType implements Event{
	UiCreated,
	playerCreated;

	@Override
	public String getName() {
		return toString();
	}

}
