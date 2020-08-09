package com.mycodefu.werekitten.netty.server;

import com.mycodefu.werekitten.network.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;

public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {
    private final ServerConnectionCallback callback;

	public NettyServerHandler(ServerConnectionCallback callback) {
		this.callback = callback;
	}

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        callback.serverConnectionClosed(ctx.channel().id());
    }

    @Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        String ip = ctx.channel().remoteAddress().toString();
        ChannelId id = ctx.channel().id();
        callback.serverConnectionMessage(id, ip, (Message)msg);
	}

	public interface ServerConnectionCallback {
        void serverConnectionOpened(ChannelId id, String remoteAddress);
        void serverConnectionMessage(ChannelId id, String sourceIpAddress, Message message);
        void serverConnectionClosed(ChannelId id);
    }
    
    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST);
        return "ws://" + location;
    }

}
