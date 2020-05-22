package com.mycodefu.werekitten.pipeline.events.chat;

import com.mycodefu.werekitten.event.ChatEventType;

public class ChatMessageRecievedEvent extends ChatEvent{
public final String message;

	@Override
	public ChatEventType getChatEvent() {
		return ChatEventType.recieved;
	}

public ChatMessageRecievedEvent(String message) {
	this.message = message;
}
}
