package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class SpaceKeyPressedEvent extends KeyboardEvent{

	public SpaceKeyPressedEvent() {
		super(KeyboardEventType.spacePressed);
	}

}
