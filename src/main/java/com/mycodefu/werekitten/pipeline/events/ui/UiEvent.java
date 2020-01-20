package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class UiEvent implements PipelineEvent{
private Event event;

@Override
public String getPipelineName() {
	return "UI";
}

@Override
public Event getEvent() {
	return event;
}

public UiEvent(UiEventType event) {
	this.event = event;
}
}