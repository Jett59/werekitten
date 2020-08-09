package com.mycodefu.werekitten.network.message;

public class ChatMessage extends Message {
public String text;

public ChatMessage(long timeStamp, String text) {
	super(MessageType.chat, timeStamp);
	this.text = text;
}
}
