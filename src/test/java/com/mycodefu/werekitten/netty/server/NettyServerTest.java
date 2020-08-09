package com.mycodefu.werekitten.netty.server;

import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.NettyClientHandler;
import com.mycodefu.werekitten.network.message.MessageBuilder;
import com.mycodefu.werekitten.network.message.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.ChannelId;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class NettyServerTest {
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
        CompletableFuture<ChannelId> connectionReceived = new CompletableFuture<>();
        CompletableFuture<ByteBuf> connectionReceivedMessage = new CompletableFuture<>();
        CompletableFuture<ChannelId> connectionClosed = new CompletableFuture<>();
        NettyServer nettyServer = new NettyServer(0, new NettyServerHandler.ServerConnectionCallback() {
            @Override
            public void serverConnectionOpened(ChannelId id, String remoteAddress) {
                System.out.println("Server received connection");
                connectionReceived.complete(id);
            }

            @Override
            public void serverConnectionMessage(ChannelId id, String sourceIpAddress, ByteBuf byteBuf) {
                connectionReceivedMessage.complete(byteBuf);
            }

            @Override
            public void serverConnectionClosed(ChannelId id) {
                connectionClosed.complete(id);
            }
        });
        nettyServer.listen();

        CompletableFuture<ByteBuf> clientMessageReceived = new CompletableFuture<>();

        AtomicReference<NettyClient> nettyClientGetter = new AtomicReference<>();
        NettyClientHandler.SocketCallback socketCallback = new NettyClientHandler.SocketCallback() {
            @Override
            public void clientDisconnected(String id) {

            }

            @Override
            public void clientConnected(String id, String remoteAddress) {
                System.out.println("Client connected");
                ByteBuf initMessage = MessageBuilder.createNewMessageBuffer(MessageType.init, 0).getBuffer();
                nettyClientGetter.get().sendMessage(initMessage);
            }

            @Override
            public void clientMessageReceived(String id, ByteBuf buffer) {
                clientMessageReceived.complete(buffer);
            }

            @Override
            public void clientError(String id, Throwable e) {

            }
        };

        NettyClient nettyClient = new NettyClient(String.format("ws://localhost:%d", nettyServer.getPort()), socketCallback);
        nettyClientGetter.set(nettyClient);

        nettyClient.connect();
//        connectionReceived.wait(1000);



    }
}