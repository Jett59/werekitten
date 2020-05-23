package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class AKeyReleasedEvent extends KeyboardEvent{

	public AKeyReleasedEvent() {
		super(KeyboardEventType.aReleased);
	}

}
