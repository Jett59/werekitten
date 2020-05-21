package com.mycodefu.werekitten.pipeline.events.chat;

import com.mycodefu.werekitten.event.ChatEventType;

public class ChatMessageSendEvent extends ChatEvent{
public final String message;
	
	@Override
	public ChatEventType getChatEvent() {
		return ChatEventType.send;
	}

public ChatMessageSendEvent(String message) {
	this.message = message;
}

}
