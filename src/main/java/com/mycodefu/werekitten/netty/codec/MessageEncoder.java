package com.mycodefu.werekitten.netty.codec;

import com.mycodefu.werekitten.network.message.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.internal.StringUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MessageEncoder extends MessageToMessageEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf message = PooledByteBufAllocator.DEFAULT.buffer();
        message.writeByte(msg.type.getCode());
        message.writeLong(msg.timeStamp);
        if (msg.type == MessageType.chat) {
            ChatMessage chatMessage = (ChatMessage) msg;
            writeStringToBuffer(message, chatMessage.text);
        } else if (msg.type == MessageType.join) {
            JoinMessage joinMessage = (JoinMessage) msg;
            writeStringToBuffer(message, joinMessage.id);
        } else if (msg.type.equals(MessageType.pang)) {
            PangMessage pangMessage = (PangMessage) msg;
            message.writeLong(pangMessage.latency);
        } else if (msg instanceof XSyncMessage) {
            XSyncMessage xSyncMessage = (XSyncMessage) msg;
            message.writeShort((short) (xSyncMessage.xSync * 10));
        }
        out.add(new BinaryWebSocketFrame(message));
    }

    private void writeStringToBuffer(ByteBuf message, String text) {
        if (StringUtil.isNullOrEmpty(text)) {
            message.writeByte(0);
        } else {
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            if (bytes.length > Byte.MAX_VALUE) {
                throw new IllegalArgumentException(String.format("chat message length may not be more than %d bytes", Byte.MAX_VALUE));
            }
            message.writeByte(bytes.length);
            message.writeBytes(bytes);
        }
    }
}
