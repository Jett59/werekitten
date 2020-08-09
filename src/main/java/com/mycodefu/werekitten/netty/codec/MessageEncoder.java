package com.mycodefu.werekitten.netty.codec;

import java.nio.charset.StandardCharsets;

import com.mycodefu.werekitten.network.message.ChatMessage;
import com.mycodefu.werekitten.network.message.Message;
import com.mycodefu.werekitten.network.message.MessageType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		out.writeByte(msg.type.getCode());
		out.writeLong(msg.timeStamp);
		if(msg.type == MessageType.chat) {
			ChatMessage chatMessage = (ChatMessage)msg;
			byte[] bytes = chatMessage.text.getBytes(StandardCharsets.UTF_8);
			if(bytes.length > Byte.MAX_VALUE) {
				throw new IllegalArgumentException(String.format("chat message length may not be more than %d bytes", bytes.length));
			}
			out.writeByte(bytes.length);
			out.writeBytes(bytes);
		}
	}

}
