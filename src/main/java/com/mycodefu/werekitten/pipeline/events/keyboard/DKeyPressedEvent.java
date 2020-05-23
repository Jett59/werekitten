package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class DKeyPressedEvent extends KeyboardEvent{

	public DKeyPressedEvent() {
		super(KeyboardEventType.dPressed);
	}

}
