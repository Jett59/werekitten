package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class AKeyPressedEvent extends KeyboardEvent{

	public AKeyPressedEvent() {
		super(KeyboardEventType.aPressed);
	}

}
