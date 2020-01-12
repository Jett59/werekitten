package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;

public class LeftKeyPressedEvent extends KeyboardEvent{

	public LeftKeyPressedEvent() {
		super(KeyboardEventType.leftPressed);
	}

}
