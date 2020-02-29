package com.mycodefu.werekitten.network.message;

import java.nio.ByteBuffer;

public class MessageBuilder {
private ByteBuffer buffer;

public MessageBuilder(ByteBuffer buffer) {
	this.buffer = buffer;
}

public MessageBuilder addDoubleAsShort(double d) {
	short s = (short)(d*10);
	buffer.putShort(s);
	return this;
}

public MessageBuilder putByte(byte b) {
	buffer.put(b);
	return this;
}

public ByteBuffer getBuffer() {
	return buffer.flip();
}

public static MessageBuilder createNewMessageBuffer(MessageType messageType, int bufferCapacity) {
	ByteBuffer buffer = ByteBuffer.allocate(bufferCapacity);
	buffer.put(messageType.getCode());
	return new MessageBuilder(buffer);
}
}
