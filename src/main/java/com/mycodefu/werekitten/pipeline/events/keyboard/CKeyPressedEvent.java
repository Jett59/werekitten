package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class CKeyPressedEvent extends KeyboardEvent{

	public CKeyPressedEvent() {
		super(KeyboardEventType.cPressed);
	}

}
