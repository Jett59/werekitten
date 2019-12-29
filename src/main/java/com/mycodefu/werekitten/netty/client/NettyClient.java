package com.mycodefu.werekitten.netty.client;

import java.net.URI;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class NettyClient {
	final URI uri;
    final String host;
    final int port;
    final boolean ssl;
    final SslContext sslCtx;
    final NettyClientHandler.SocketCallback callback;
    private Channel channel;
    private NioEventLoopGroup group;
    private boolean connected;

    /**
     * @param url e.g. ws://127.0.0.1:8080/websocket
     */
    public WebSocketClient(String url, WebSocketClientHandler.SocketCallback callback) {
        this.callback = callback;
        try {
            this.uri = new URI(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("URL invalid. Unable to parse into URI.");
        }

        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        this.host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                this.port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                this.port = 443;
            } else {
                this.port = -1;
            }
        } else {
            this.port = uri.getPort();
        }

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("Only ws and wss scheme URLs are supported!");
        }

        ssl = "wss".equalsIgnoreCase(scheme);
        if (ssl) {
            try {
                this.sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } catch (SSLException e) {
                throw new IllegalStateException("Unable to create an SSL context for making WSS scheme connections.", e);
            }
        } else {
            this.sslCtx = null;
        }
    }

    public boolean sendMessage(String message) {
        if (connected) {
            WebSocketFrame frame = new TextWebSocketFrame(message);
            channel.writeAndFlush(frame);
            return true;
        } else {
            return false;
        }
    }

    public void disconnect() {
        if (connected) {
            group.shutdownGracefully();
            connected = false;
        }
    }

    public void connect() {
        if (!connected) {
            this.group = new NioEventLoopGroup();
            String id = "";
            try {
                final WebSocketClientHandler handler =
                        new WebSocketClientHandler(
                                WebSocketClientHandshakerFactory.newHandshaker(
                                        uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()),
                                this.callback
                        );
                id = handler.getId();

                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ChannelPipeline p = ch.pipeline();
                                if (sslCtx != null) {
                                    p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                                }
                                p.addLast(
                                        new HttpClientCodec(),
                                        new HttpObjectAggregator(8192),
                                        WebSocketClientCompressionHandler.INSTANCE,
                                        handler);
                            }
                        });

                channel = b.connect(host, port).sync().channel();
                handler.handshakeFuture().sync();

                connected = true;

            } catch (Exception e) {
                this.callback.clientError(id, e);
                throw new RuntimeException("Failed to connect", e);
            }
        }
    }

}
