package com.mycodefu.werekitten.netty.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.junit.jupiter.api.Test;

import com.mycodefu.werekitten.network.message.ChatMessage;
import com.mycodefu.werekitten.network.message.MessageType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class MessageDecoderTest {
@Test
void decode_chatMessage_0charTextSize() throws Exception{
	ByteBuf in = PooledByteBufAllocator.DEFAULT.buffer();
	in.writeByte(MessageType.chat.getCode());
	long timeStamp = System.currentTimeMillis();
	in.writeLong(timeStamp);
	in.writeByte(0);
	MessageDecoder decoder = new MessageDecoder();
	List<Object> out = new ArrayList<>();
	decoder.decode(null, new BinaryWebSocketFrame(in), out);
	ChatMessage message = (ChatMessage)out.get(0);
	assertEquals(MessageType.chat, message.type, "error: types do not match");
	assertEquals(timeStamp, message.timeStamp, "error: time stamps do not match");
	assertEquals("", message.text, "error: text does not match");
}
}
