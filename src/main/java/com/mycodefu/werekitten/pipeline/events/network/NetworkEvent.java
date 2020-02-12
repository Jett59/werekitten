package com.mycodefu.werekitten.pipeline.events.network;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.NetworkEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class NetworkEvent implements PipelineEvent {
	@Override
	public String getPipelineName() {
		return "network";
	}

	@Override
	public Event getEvent() {
		return getNetworkEvent();
	}

	protected abstract NetworkEventType getNetworkEvent();
}
