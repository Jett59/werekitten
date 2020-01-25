package com.mycodefu.werekitten.pipeline.events.network;

import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class NetworkEvent implements PipelineEvent {
	@Override
	public String getPipelineName() {
		return "network";
	}

}
