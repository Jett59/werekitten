package com.mycodefu.werekitten.netty.codec;

import java.util.List;

import com.mycodefu.werekitten.network.message.ChatMessage;
import com.mycodefu.werekitten.network.message.Message;
import com.mycodefu.werekitten.network.message.MessageType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.CharsetUtil;

public class MessageDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

	@Override
	protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
		ByteBuf in = msg.content();
		MessageType type = MessageType.forCode(in.readByte());
		long timeStamp = in.readLong();
		if(type.equals(MessageType.chat)) {
			byte stringLength = in.readByte();
			byte[] bytes = new byte[stringLength];
			in.readBytes(bytes);
			String text = new String(bytes, CharsetUtil.UTF_8);
			out.add(new ChatMessage(timeStamp, text));
		}else {
			out.add(new Message(type, timeStamp));
		}
	}
}
