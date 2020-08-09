package com.mycodefu.werekitten.netty.server;

import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.NettyClientHandler;
import com.mycodefu.werekitten.network.message.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelId;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static com.mycodefu.werekitten.network.message.MessageBuilder.MESSAGE_TYPE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NettyServerTest {
    private static final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    //TODO: Change to using WebSocketServerProtocolHandler instead of manual upgrade in NettyServerHandler
    //ref: https://netty.io/4.0/api/io/netty/handler/codec/http/websocketx/WebSocketServerProtocolHandler.html
    //TODO: Implement a MessageDecoder which extends MessageToMessageDecoder
    //ref: https://netty.io/4.0/api/io/netty/handler/codec/MessageToMessageDecoder.html
    //ref: https://github.com/blynkkk/blynk-server/blob/25aee285f4a7845f7128b6989314d05f2b08ad80/server/core/src/main/java/cc/blynk/server/core/protocol/handlers/decoders/MessageDecoder.java
    //TODO: Implement a MessageEncoder which extends MessageToByteEncoder<PlayerMessage>
    //ref: https://netty.io/4.0/api/io/netty/handler/codec/MessageToByteEncoder.html
    //e.g.: https://github.com/blynkkk/blynk-server/blob/25aee285f4a7845f7128b6989314d05f2b08ad80/server/core/src/main/java/cc/blynk/server/core/protocol/handlers/encoders/MessageEncoder.java

    @Test
    void initHandshakeTest() throws InterruptedException {
        Object lock = new Object();

        AtomicReference<String> serverMessage = new AtomicReference<>();
        AtomicReference<String> clientMessage = new AtomicReference<>();

        AtomicReference<NettyServer> nettyServerGetter = new AtomicReference<>();
        NettyServer nettyServer = new NettyServer(0, new NettyServerHandler.ServerConnectionCallback() {
            @Override
            public void serverConnectionOpened(ChannelId id, String remoteAddress) {
                System.out.println("Server received connection");
            }

            @Override
            public void serverConnectionMessage(ChannelId id, String sourceIpAddress, ByteBuf byteBuf) {
                String message = byteBuf.toString(StandardCharsets.UTF_8);
                System.out.printf("Received message %s\n", message);
                serverMessage.set(message);
                nettyServerGetter.get().sendMessage(id, createMessageByteBuf("Initialized!"));
            }

            @Override
            public void serverConnectionClosed(ChannelId id) {
                System.out.println("Server connection closed.");
            }
        });
        nettyServerGetter.set(nettyServer);
        nettyServer.listen();

        AtomicReference<NettyClient> nettyClientGetter = new AtomicReference<>();
        NettyClientHandler.SocketCallback socketCallback = new NettyClientHandler.SocketCallback() {
            @Override
            public void clientDisconnected(String id) {
                System.out.println("Client disconnected.");
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override
            public void clientConnected(String id, String remoteAddress) {
                System.out.println("Client connected");
                ByteBuf buffer = createMessageByteBuf("Initializing...");
                nettyClientGetter.get().sendMessage(buffer);
            }

            @Override
            public void clientMessageReceived(String id, ByteBuf buffer) {
                String message = buffer.toString(StandardCharsets.UTF_8);
                clientMessage.set(message);
                System.out.printf("Client received message: %s\n", message);
                nettyClientGetter.get().disconnect();
            }

            @Override
            public void clientError(String id, Throwable e) {
                System.out.println("Error in client:");
                e.printStackTrace();
            }
        };

        NettyClient nettyClient = new NettyClient(String.format("ws://localhost:%d", nettyServer.getPort()), socketCallback);
        nettyClientGetter.set(nettyClient);

        nettyClient.connect();

        synchronized (lock) {
            lock.wait(10000);
            System.out.flush();
        }

        assertEquals("\u0007\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u000FInitializing...", serverMessage.get());
        assertEquals("\u0007\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\fInitialized!", clientMessage.get());
    }

    private ByteBuf createMessageByteBuf(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        int size = MESSAGE_TYPE_SIZE + Long.BYTES + 1 + bytes.length;
        ByteBuf buffer = allocator.buffer(size, size);
        buffer.writeByte(MessageType.chat.getCode());
        buffer.writeLong(0);
        buffer.writeByte(bytes.length);
        buffer.writeBytes(bytes);
        return buffer;
    }
}