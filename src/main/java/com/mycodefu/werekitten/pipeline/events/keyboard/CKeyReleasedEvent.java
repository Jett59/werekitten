package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class CKeyReleasedEvent extends KeyboardEvent{

	public CKeyReleasedEvent() {
		super(KeyboardEventType.cReleased);
	}

}
