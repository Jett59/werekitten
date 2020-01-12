package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;

public class SpaceKeyReleasedEvent extends KeyboardEvent{

	public SpaceKeyReleasedEvent() {
		super(KeyboardEventType.spaceReleased);
	}

}
