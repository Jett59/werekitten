package com.mycodefu.werekitten.netty.server;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {
	private final ChannelId id;
    private final ServerConnectionCallback callback;

	public NettyServerHandler(ChannelId id, ServerConnectionCallback callback) {
		this.id = id;
		this.callback = callback;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketRequest(ctx, (WebSocketFrame) msg);
        }
	}

	public interface ServerConnectionCallback {
        void connectionReceived(ChannelId id);
        void messageReceived(ChannelId id, String sourceIpAddress, String message);
    }

	private void handleWebSocketRequest(ChannelHandlerContext channelHandlerContext, WebSocketFrame msg) {
        String ip = channelHandlerContext.channel().remoteAddress().toString();
        callback.messageReceived(id, ip, msg.content().toString(CharsetUtil.UTF_8));
    }

    private void handleHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest msg) {
        String ip = channelHandlerContext.channel().remoteAddress().toString();

        System.out.println("Received HTTP Request from " + ip);

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(msg), null, true);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(msg);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channelHandlerContext.channel());
        } else {
            ChannelFuture channelFuture = handshaker.handshake(channelHandlerContext.channel(), msg);
            if (channelFuture.isSuccess()) {
                System.out.println(channelHandlerContext.channel() + " Connected");

                callback.connectionReceived(id);
            }
        }
    }
    
    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST);
        return "ws://" + location;
    }

}
