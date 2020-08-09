package com.mycodefu.werekitten.network.message;

public class ChatMessage extends Message {
public String text;

public ChatMessage(MessageType type, long timeStamp, String text) {
	super(type, timeStamp);
	this.text = text;
}
}
