package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class LeftKeyReleasedEvent extends KeyboardEvent{

	public LeftKeyReleasedEvent() {
		super(KeyboardEventType.leftReleased);
	}

}
