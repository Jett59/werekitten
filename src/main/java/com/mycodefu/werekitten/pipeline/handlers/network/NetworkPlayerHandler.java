package com.mycodefu.werekitten.pipeline.handlers.network;

import com.mycodefu.werekitten.netty.server.NettyServer;
import com.mycodefu.werekitten.netty.server.NettyServerHandler;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkServerListeningEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;
import io.netty.channel.ChannelId;

public class NetworkPlayerHandler implements PipelineHandler {
    private final NetworkPlayerHelper networkPlayerHelper;
    NettyServer server;

    public NetworkPlayerHandler() {
        this.networkPlayerHelper = new NetworkPlayerHelper();
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("network")) {
            switch (event.getEvent().getName()) {
                case "start": {
                    System.out.println("Starting network listener...");
                    server = new NettyServer(0, new NettyServerHandler.ServerConnectionCallback() {
                        @Override
                        public void serverConnectionOpened(ChannelId id) {
                            networkPlayerHelper.createNetworkPlayer(id.asLongText(), context, (message) -> server.sendMessage(id, message));
                        }

                        @Override
                        public void serverConnectionMessage(ChannelId id, String sourceIpAddress, String message) {
                            networkPlayerHelper.applyNetworkMessageToPlayer(message, id.asLongText(), context);
                        }

                        @Override
                        public void serverConnectionClosed(ChannelId id) {
                            networkPlayerHelper.destroyNetworkPlayer(id.asLongText(), context);
                        }
                    });
                    server.listen();
                    context.postEvent(new NetworkServerListeningEvent(server.getPort()));
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
        } else {
            throw new IllegalArgumentException("the network player handler is not allowed to be in the pipeline " + event.getPipelineName());
        }
    }


}