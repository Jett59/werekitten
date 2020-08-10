package com.mycodefu.werekitten.network.message;

public class PangMessage extends Message {
public long latency;

public PangMessage(long timeStamp, long latency) {
	super(MessageType.pang, timeStamp);
	this.latency = latency;
}
public PangMessage(long latency) {
	super(MessageType.pang);
	this.latency = latency;
}
}
