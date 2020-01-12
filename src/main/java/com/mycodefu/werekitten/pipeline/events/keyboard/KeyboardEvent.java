package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public class KeyboardEvent implements PipelineEvent{
private Event event;

@Override
public String getPipelineName() {
	return "keyboard";
}

@Override
public Event getEvent() {
	return event;
}

public KeyboardEvent(Event event) {
	this.event = event;
}
}
