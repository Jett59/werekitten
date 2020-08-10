package com.mycodefu.werekitten.netty.client;

import com.mycodefu.werekitten.network.message.Message;
import io.netty.buffer.ByteBuf;

public interface SocketCallback {
    void clientDisconnected(String id);

    void clientConnected(String id, String remoteAddress);

    void clientMessageReceived(String id, Message buffer);

    void clientError(String id, Throwable e);
}
