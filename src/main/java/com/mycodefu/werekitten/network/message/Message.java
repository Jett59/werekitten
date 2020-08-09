package com.mycodefu.werekitten.network.message;

public class Message {
public MessageType type;
public long timeStamp;

public Message(MessageType type, long timeStamp) {
	this.timeStamp = timeStamp;
	this.type = type;
}
}
