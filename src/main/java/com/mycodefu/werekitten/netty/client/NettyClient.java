package com.mycodefu.werekitten.netty.client;

import com.mycodefu.werekitten.netty.codec.MessageDecoder;
import com.mycodefu.werekitten.netty.codec.MessageEncoder;
import com.mycodefu.werekitten.network.message.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

public class NettyClient {
    final URI uri;
    final String host;
    final int port;
    final boolean ssl;
    final SslContext sslCtx;
    final SocketCallback callback;
    private Channel channel;
    private NioEventLoopGroup group;
    private boolean connected;

    /**
     * @param url e.g. ws://127.0.0.1:8080/websocket
     */
    public NettyClient(String url, SocketCallback callback) {
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

    public boolean sendMessage(Message message) {
        if (connected) {
            System.out.println("Writing message...");
            channel.writeAndFlush(message);
            return true;
        } else {
            return false;
        }
    }

    public boolean sendMessage(ByteBuf message) {
        if (connected) {
            WebSocketFrame frame = new BinaryWebSocketFrame(message);
            System.out.println("Writing message...");
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
            AtomicReference<String> idRef = new AtomicReference<>("not_connected");
            try {
                SocketCallback callback = new SocketCallback() {
                    @Override
                    public void clientDisconnected(String id) {
                        NettyClient.this.callback.clientDisconnected(id);
                    }

                    @Override
                    public void clientConnected(String id, String remoteAddress) {
                        connected = true;
                        idRef.set(id);
                        NettyClient.this.callback.clientConnected(id, remoteAddress);
                    }

                    @Override
                    public void clientMessageReceived(String id, Message buffer) {
                        NettyClient.this.callback.clientMessageReceived(id, buffer);
                    }

                    @Override
                    public void clientError(String id, Throwable e) {
                        NettyClient.this.callback.clientError(id, e);
                    }
                };

                WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
                final NettyClientWebSocketUpgradeHandler websocketUpgradeHandler = new NettyClientWebSocketUpgradeHandler(
                        handshaker,
                        callback
                );
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ChannelPipeline pipeline = ch.pipeline();
                                if (sslCtx != null) {
                                    pipeline.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                                }
                                pipeline.addLast(new HttpClientCodec());
                                pipeline.addLast(new HttpObjectAggregator(8192));
                                pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);
                                pipeline.addLast(websocketUpgradeHandler);
                                pipeline.addLast(new MessageEncoder());
                                pipeline.addLast(new MessageDecoder());
                                pipeline.addLast(new NettyClientMessageHandler(callback));
                            }
                        });

                channel = b.connect(host, port).sync().channel();
                websocketUpgradeHandler.handshakeFuture().sync();

            } catch (Exception e) {
                this.callback.clientError(idRef.get(), e);
                throw new RuntimeException("Failed to connect", e);
            }
        }
    }

}
