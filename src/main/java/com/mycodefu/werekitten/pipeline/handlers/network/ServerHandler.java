package com.mycodefu.werekitten.pipeline.handlers.network;

import com.mycodefu.werekitten.netty.server.NettyServer;
import com.mycodefu.werekitten.netty.server.NettyServerHandler;
import com.mycodefu.werekitten.network.NetworkUtils;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkServerListeningEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent.ConnectionType.client;

public class ServerHandler implements PipelineHandler {
    private final NetworkPlayerHelper networkPlayerHelper;
    NettyServer server;

    public ServerHandler() {
        this.networkPlayerHelper = new NetworkPlayerHelper();
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("pipeline") && event instanceof NetworkEvent) {
            switch (event.getEvent().getName()) {
                case "start": {
                    System.out.println("Starting network listener...");
                    server = new NettyServer(context.getListeningPort(), new NettyServerHandler.ServerConnectionCallback() {
                        ConcurrentMap<ChannelId, NetworkPlayerHelper.NetworkPlayerMessageSender> senders = new ConcurrentHashMap<>();

                        @Override
                        public void serverConnectionOpened(ChannelId id, String remoteAddress) {
                            senders.put(id, (playerMessage) -> server.sendMessage(id, playerMessage));
                            context.postEvent(new NetworkConnectionEstablishedEvent(client, remoteAddress));
                        }

                        @Override
                        public void serverConnectionMessage(ChannelId id, String sourceIpAddress, ByteBuf message) {
                            networkPlayerHelper.applyNetworkMessageToPlayer(message, id.asLongText(), context, senders.get(id), true);
                        }

                        @Override
                        public void serverConnectionClosed(ChannelId id) {
                            System.out.println("server destroying player");
                            networkPlayerHelper.destroyNetworkPlayer(id.asLongText(), context);
                        }
                    });
                    server.listen();
                    String internetIpAddress = NetworkUtils.getInternetIpAddress();
                    String wsAddress = String.format("ws://%s:%d", internetIpAddress, server.getPort());
                    context.postEvent(new NetworkServerListeningEvent(wsAddress));
                    break;
                }
                case "stop": {
                    System.out.println("Stopping network listener...");
                    server.close();
                    break;
                }

                default:
                    break;
            }
        }
    }


}
