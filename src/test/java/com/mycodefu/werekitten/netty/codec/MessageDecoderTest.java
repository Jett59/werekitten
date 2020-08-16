package com.mycodefu.werekitten.netty.codec;

import com.mycodefu.werekitten.network.message.ChatMessage;
import com.mycodefu.werekitten.network.message.JoinMessage;
import com.mycodefu.werekitten.network.message.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageDecoderTest {
    @Test
    void decode_chatMessage_0charTextSize() throws Exception {
        ByteBuf in = PooledByteBufAllocator.DEFAULT.buffer();
        in.writeByte(MessageType.chat.getCode());
        long timeStamp = System.currentTimeMillis();
        in.writeLong(timeStamp);
        in.writeByte(0);
        MessageDecoder decoder = new MessageDecoder();
        List<Object> out = new ArrayList<>();
        decoder.decode(null, new BinaryWebSocketFrame(in), out);
        ChatMessage message = (ChatMessage) out.get(0);
        assertEquals(MessageType.chat, message.type, "error: types do not match");
        assertEquals(timeStamp, message.timeStamp, "error: time stamps do not match");
        assertEquals("", message.text, "error: text does not match");
    }
    @Test
    void decode_joinMessage() throws Exception {
        ByteBuf in = PooledByteBufAllocator.DEFAULT.buffer();
        in.writeByte(MessageType.join.getCode());
        long timeStamp = System.currentTimeMillis();
        in.writeLong(timeStamp);
        in.writeByte(10);
		String id = "1234567890";
		in.writeBytes(id.getBytes(StandardCharsets.UTF_8));

        MessageDecoder decoder = new MessageDecoder();
        List<Object> out = new ArrayList<>();
        decoder.decode(null, new BinaryWebSocketFrame(in), out);
        JoinMessage message = (JoinMessage) out.get(0);
        assertEquals(MessageType.join, message.type, "error: types do not match");
        assertEquals(timeStamp, message.timeStamp, "error: time stamps do not match");
        assertEquals(id, message.id, "error: id does not match");
    }
    @Test
    void decode_joinMessage_0charTextSize() throws Exception {
        ByteBuf in = PooledByteBufAllocator.DEFAULT.buffer();
        in.writeByte(MessageType.join.getCode());
        long timeStamp = System.currentTimeMillis();
        in.writeLong(timeStamp);
        in.writeByte(0);

        MessageDecoder decoder = new MessageDecoder();
        List<Object> out = new ArrayList<>();
        decoder.decode(null, new BinaryWebSocketFrame(in), out);
        JoinMessage message = (JoinMessage) out.get(0);
        assertEquals(MessageType.join, message.type, "error: types do not match");
        assertEquals(timeStamp, message.timeStamp, "error: time stamps do not match");
        assertEquals("", message.id, "error: id does not match");
    }
}
