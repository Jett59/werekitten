package com.mycodefu.werekitten.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;

public class NettyClientWebSocketUpgradeHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
	private final WebSocketClientHandshaker handshaker;
    private final SocketCallback callback;
    private ChannelPromise handshakeFuture;

    public NettyClientWebSocketUpgradeHandler(WebSocketClientHandshaker handshaker, SocketCallback callback) {
        this.handshaker = handshaker;
        this.callback = callback;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        callback.clientDisconnected(ctx.channel().id().asShortText());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {
        Channel ch = ctx.channel();
        String id = ctx.channel().id().asShortText();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, msg);
                System.out.println("WebSocket Client connected!");
                handshakeFuture.setSuccess();
                callback.clientConnected(id, ctx.channel().remoteAddress().toString());

            } catch (WebSocketHandshakeException e) {
                System.out.println("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
                callback.clientError(id, e);
            }
            return;
        } else {
            ByteBuf content = msg.content();
            IllegalStateException illegalStateException = new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + msg.status() +
                            ", content=" + content.toString(CharsetUtil.UTF_8) + ')');
            callback.clientError(id, illegalStateException);
            throw illegalStateException;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
        callback.clientError(ctx.channel().id().asShortText(), cause);
    }

}
