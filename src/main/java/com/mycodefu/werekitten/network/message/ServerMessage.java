package com.mycodefu.werekitten.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ServerMessage {
public static ByteBuf introductionMessage(int id, IntroductionType introductionType) {
	ByteBuf value = ByteBufAllocator.DEFAULT.buffer(2+Integer.BYTES);
	value.writeByte(introductionType.code);
	value.writeByte(Integer.BYTES);
	value.writeInt(id);
	return value;
}

public static enum IntroductionType {
	HOST((byte)'A'),
	JOIN((byte)'B');
	private byte code;
	
	private IntroductionType(byte code) {
		this.code = code;
	}
}
}
