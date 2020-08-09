package com.mycodefu.werekitten.netty.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.junit.jupiter.api.Test;

import com.mycodefu.werekitten.network.message.ChatMessage;
import com.mycodefu.werekitten.network.message.Message;
import com.mycodefu.werekitten.network.message.MessageType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class MessageEncoderTest {
@Test
void encode_chatMessage_0charTextSize() throws Exception{
	ChatMessage message = new ChatMessage(System.currentTimeMillis(), "");
	MessageEncoder encoder = new MessageEncoder();
	ArrayList<Object> out = new ArrayList<>();
	encoder.encode(null, message, out);
	ByteBuf buf = ((BinaryWebSocketFrame)out.get(0)).content();
	assertEquals(message.type.getCode(), buf.readByte());
	assertEquals(message.timeStamp, buf.readLong());
	byte textSize = buf.readByte();
	byte[] bytes = new byte[textSize];
	buf.readBytes(bytes);
	String decodedString = new String(bytes, StandardCharsets.UTF_8);
	assertEquals(message.text, decodedString);
}
@Test
void encode_chatMessage_standardMessageSize() throws Exception{
	ChatMessage message = new ChatMessage(System.currentTimeMillis(), "test message");
	MessageEncoder encoder = new MessageEncoder();

	ArrayList<Object> out = new ArrayList<>();
	encoder.encode(null, message, out);
	ByteBuf buf = ((BinaryWebSocketFrame)out.get(0)).content();

	assertEquals(message.type.getCode(), buf.readByte());
	assertEquals(message.timeStamp, buf.readLong());
	byte textSize = buf.readByte();
	byte[] bytes = new byte[textSize];
	buf.readBytes(bytes);
	String decodedString = new String(bytes, StandardCharsets.UTF_8);
	assertEquals(message.text, decodedString);
}
@Test
void encode_chatMessage_127CharTextSize() throws Exception{
	ChatMessage message = new ChatMessage(System.currentTimeMillis(), "r".repeat(127));
	MessageEncoder encoder = new MessageEncoder();

	ArrayList<Object> out = new ArrayList<>();
	encoder.encode(null, message, out);
	ByteBuf buf = ((BinaryWebSocketFrame)out.get(0)).content();

	assertEquals(message.type.getCode(), buf.readByte());
	assertEquals(message.timeStamp, buf.readLong());
	byte textSize = buf.readByte();
	byte[] bytes = new byte[textSize];
	buf.readBytes(bytes);
	String decodedString = new String(bytes, StandardCharsets.UTF_8);
	assertEquals(message.text, decodedString);
}
@Test
void encode_chatMessage_massiveSize() throws Exception{
	ChatMessage message = new ChatMessage(System.currentTimeMillis(), "r".repeat(128));
	MessageEncoder encoder = new MessageEncoder();
	assertThrows(IllegalArgumentException.class, ()->{
		ArrayList<Object> out = new ArrayList<>();
		encoder.encode(null, message, out);
	});
}
@Test
void encode_standardMessage() throws Exception {
	Message message = new Message(MessageType.init, System.currentTimeMillis());
	MessageEncoder encoder = new MessageEncoder();

	ArrayList<Object> out = new ArrayList<>();
	encoder.encode(null, message, out);
	ByteBuf buf = ((BinaryWebSocketFrame)out.get(0)).content();

	assertEquals(message.type.getCode(), buf.readByte());
	assertEquals(message.timeStamp, buf.readLong());
}
}
