package com.mycodefu.werekitten.pipeline.events.chat;

import com.mycodefu.werekitten.event.ChatEventType;

public class ChatMessageRecievedEvent extends ChatEvent{
	@Override
	public ChatEventType getChatEvent() {
		return ChatEventType.recieved;
	}
}
