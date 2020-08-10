package com.mycodefu.werekitten.netty.server;

import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.SocketCallback;
import com.mycodefu.werekitten.network.message.ChatMessage;
import com.mycodefu.werekitten.network.message.Message;
import com.mycodefu.werekitten.network.message.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelId;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
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
        AtomicBoolean finished = new AtomicBoolean();

        AtomicReference<String> serverMessage = new AtomicReference<>();
        AtomicReference<String> clientMessage = new AtomicReference<>();

        AtomicReference<NettyServer> nettyServerGetter = new AtomicReference<>();
        NettyServer nettyServer = new NettyServer(0, new NettyServerHandler.ServerConnectionCallback() {
            @Override
            public void serverConnectionOpened(ChannelId id, String remoteAddress) {
                System.out.println("Server received connection");
            }

            @Override
            public void serverConnectionMessage(ChannelId id, String sourceIpAddress, Message message) {
                System.out.printf("Received message %s\n", message.toString());
                serverMessage.set(message.toString());
                nettyServerGetter.get().sendMessage(id, createMessage("Initialized!"));
            }

            @Override
            public void serverConnectionClosed(ChannelId id) {
                System.out.println("Server connection closed.");
            }
        });
        nettyServerGetter.set(nettyServer);
        nettyServer.listen();

        AtomicReference<NettyClient> nettyClientGetter = new AtomicReference<>();
        SocketCallback socketCallback = new SocketCallback() {
            @Override
            public void clientDisconnected(String id) {
                System.out.println("Client disconnected.");
                finished.set(true);
            }

            @Override
            public void clientConnected(String id, String remoteAddress) {
                System.out.println("Client connected");
                ByteBuf message = createMessageByteBuf("Initializing...");
                nettyClientGetter.get().sendMessage(message);
            }

            @Override
            public void clientMessageReceived(String id, Message message) {
                clientMessage.set(message.toString());
                System.out.printf("Client received message: %s\n", message);
                nettyClientGetter.get().disconnect();
            }

            @Override
            public void clientError(String id, Throwable e) {
                System.out.println("Error in client:");
                e.printStackTrace();
            }
        };

        NettyClient nettyClient = new NettyClient(String.format("ws://127.0.0.1:%d", nettyServer.getPort()), socketCallback);
        nettyClientGetter.set(nettyClient);

        nettyClient.connect();

        int wait300Seconds = 30;
        while (wait300Seconds > 0 && !finished.get()) {
            Thread.sleep(300);
            wait300Seconds--;
        }
        System.out.flush();

        assertEquals("ChatMessage{text='Initializing...', type=chat, timeStamp=0}", serverMessage.get());
        assertEquals("ChatMessage{text='Initialized!', type=chat, timeStamp=0}", clientMessage.get());
    }

    private Message createMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(0, message);
        return chatMessage;
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