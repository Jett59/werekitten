package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public class KeyboardEvent implements PipelineEvent{
private KeyboardEventType event;

@Override
public String getPipelineName() {
	return "pipeline";
}

@Override
public Event getEvent() {
	return event;
}

public KeyboardEventType getKeyboardEvent() {
	return event;
}

public KeyboardEvent(KeyboardEventType event) {
	this.event = event;
}
}
