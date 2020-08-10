package com.mycodefu.werekitten.netty.client;

import com.mycodefu.werekitten.network.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientMessageHandler extends SimpleChannelInboundHandler<Message> {
    private SocketCallback callback;
    public NettyClientMessageHandler(SocketCallback callback) {
        this.callback = callback;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        callback.clientMessageReceived(ctx.channel().id().asShortText(), msg);
    }
}
