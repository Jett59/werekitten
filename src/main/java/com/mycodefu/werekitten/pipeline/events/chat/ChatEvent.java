package com.mycodefu.werekitten.pipeline.events.chat;

import com.mycodefu.werekitten.event.ChatEventType;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class ChatEvent implements PipelineEvent{

	@Override
	public String getPipelineName() {
		return "pipeline";
	}

public abstract ChatEventType getChatEvent();

	@Override
	public Event getEvent() {
		return getChatEvent();
	}

}
