package com.mycodefu.werekitten.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class MessageBuilder {
    private static final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
    public static final int MESSAGE_TYPE_SIZE = 1;
    public static final int MESSAGE_TIME_SIZE = 8;
    private ByteBuf buffer;

    private MessageBuilder(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public MessageBuilder addDoubleAsShort(double d) {
        short s = (short) (d * 10);
        buffer.writeShort(s);
        return this;
    }

    public MessageBuilder putByte(byte b) {
        buffer.writeByte(b);
        return this;
    }

    private MessageBuilder putLong(long value) {
        buffer.writeLong(value);
        return this;
    }

    public MessageBuilder putBytes(byte[] bytes) {
    	for(byte b : bytes) {
    		putByte(b);
    	}
    	return this;
    }
    
    public ByteBuf getBuffer() {
        return buffer;
    }

    public static MessageBuilder createNewMessageBuffer(MessageType messageType, int bufferCapacity) {
        int size = MESSAGE_TYPE_SIZE + Long.BYTES + bufferCapacity;
        ByteBuf buffer = allocator.buffer(size, size);

        return new MessageBuilder(buffer)
                .putByte(messageType.getCode())
                .putLong(System.currentTimeMillis());
    }
}
