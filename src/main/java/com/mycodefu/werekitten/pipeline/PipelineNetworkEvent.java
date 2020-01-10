package com.mycodefu.werekitten.pipeline;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.NetworkEventType;

public class PipelineNetworkEvent implements PipelineEvent{
private Event event;

	@Override
	public PipelineEventType getEventType() {
		return PipelineEventType.Network;
	}

	@Override
	public Event getEvent() {
		return event;
	}

public PipelineNetworkEvent(NetworkEventType eventType) {
	event = eventType;
}
}
