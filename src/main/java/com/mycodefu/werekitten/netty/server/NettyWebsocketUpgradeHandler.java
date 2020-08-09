package com.mycodefu.werekitten.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;

public class NettyWebsocketUpgradeHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        handleHttpRequest(ctx, msg);
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
            }
        }
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST);
        return "ws://" + location;
    }

}
