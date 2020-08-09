package com.mycodefu.werekitten.network.message;

public class XSyncMessage extends Message{
public double xSync;

public XSyncMessage(MessageType type, long timeStamp, double xSync) {
	super(type, timeStamp);
	this.xSync = xSync;
}
}
