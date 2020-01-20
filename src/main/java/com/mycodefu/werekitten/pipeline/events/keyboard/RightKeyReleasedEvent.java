package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class RightKeyReleasedEvent extends KeyboardEvent{

	public RightKeyReleasedEvent() {
		super(KeyboardEventType.rightReleased);
	}

}
