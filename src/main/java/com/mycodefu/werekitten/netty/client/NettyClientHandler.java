package com.mycodefu.werekitten.netty.client;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {
	private final WebSocketClientHandshaker handshaker;
    private final String id;
    private SocketCallback callback;
    private ChannelPromise handshakeFuture;

    public NettyClientHandler(WebSocketClientHandshaker handshaker, SocketCallback callback) {
        this.handshaker = handshaker;
        this.callback = callback;
        this.id = UUID.randomUUID().toString();
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
        callback.clientDisconnected(id);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("WebSocket Client connected!");
                handshakeFuture.setSuccess();
                callback.clientConnected(id);

            } catch (WebSocketHandshakeException e) {
                System.out.println("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
                callback.clientError(id, e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            ByteBuf content = response.content();
            IllegalStateException illegalStateException = new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + content.toString(CharsetUtil.UTF_8) + ')');
            callback.clientError(id, illegalStateException);
            throw illegalStateException;
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String text = textFrame.text();
//            System.out.println("WebSocket Client received message: " + text);
            callback.clientMessageReceived(id, text);

        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");
            ch.close();
            callback.clientDisconnected(id);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
        callback.clientError(id, cause);
    }

    public String getId() {
        return id;
    }

    public interface SocketCallback {
        void clientDisconnected(String id);
        void clientConnected(String id);
        void clientMessageReceived(String id, String text);
        void clientError(String id, Throwable e);
    }
}
