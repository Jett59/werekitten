package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class RightKeyPressedEvent extends KeyboardEvent{

	public RightKeyPressedEvent() {
		super(KeyboardEventType.rightPressed);
	}

}
