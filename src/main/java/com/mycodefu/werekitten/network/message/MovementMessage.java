package com.mycodefu.werekitten.network.message;

public class MovementMessage extends Message{
public double xSync;

public MovementMessage(MessageType type, long timeStamp, double xSync) {
	super(type, timeStamp);
	this.xSync = xSync;
}
}
