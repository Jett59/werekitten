package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class DKeyReleasedEvent extends KeyboardEvent{

	public DKeyReleasedEvent() {
		super(KeyboardEventType.dReleased);
	}

}
