package com.mycodefu.werekitten.netty.codec;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.mycodefu.werekitten.network.message.ChatMessage;
import com.mycodefu.werekitten.network.message.Message;
import com.mycodefu.werekitten.network.message.MessageType;
import com.mycodefu.werekitten.network.message.XSyncMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class MessageEncoder extends MessageToMessageEncoder<Message> {
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		ByteBuf message = PooledByteBufAllocator.DEFAULT.buffer();
		message.writeByte(msg.type.getCode());
		message.writeLong(msg.timeStamp);
		if(msg.type == MessageType.chat) {
			ChatMessage chatMessage = (ChatMessage)msg;
			byte[] bytes = chatMessage.text.getBytes(StandardCharsets.UTF_8);
			if(bytes.length > Byte.MAX_VALUE) {
				throw new IllegalArgumentException(String.format("chat message length may not be more than %d bytes", Byte.MAX_VALUE));
			}
			message.writeByte(bytes.length);
			message.writeBytes(bytes);
		}else if(msg instanceof XSyncMessage) {
			XSyncMessage xSyncMessage = (XSyncMessage)msg;
			message.writeShort((short)(xSyncMessage.xSync*10));
		}
		out.add(new BinaryWebSocketFrame(message));
	}
}
