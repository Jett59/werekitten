package com.mycodefu.werekitten.pipeline.handlers.network;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.event.NetworkEventType;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.netty.server.NettyServer;
import com.mycodefu.werekitten.netty.server.NettyServerHandler;
import com.mycodefu.werekitten.network.NetworkUtils;
import com.mycodefu.werekitten.network.message.MessageBuilder;
import com.mycodefu.werekitten.network.message.MessageType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkEvent;
import com.mycodefu.werekitten.pipeline.events.player.PlayerEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkServerListeningEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;
import com.mycodefu.werekitten.preferences.Preferences;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelId;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent.ConnectionType.client;

public class ServerHandler implements PipelineHandler {
    private final NetworkPlayerHelper networkPlayerHelper;
    private NettyServer server;
    private List<ChannelId> channelIds;

    public ServerHandler() {
        this.networkPlayerHelper = new NetworkPlayerHelper();
        this.channelIds = new CopyOnWriteArrayList<>();
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof NetworkEvent) {
            NetworkEvent networkEvent = (NetworkEvent) event;
            switch (networkEvent.getNetworkEvent()) {
                case start: {
                	String portPreference = context.getPreferences().get(Preferences.LISTENING_PORT_PREFERENCE);
                	int port = portPreference == null || portPreference == "" ? context.getListeningPort() : Integer.parseInt(portPreference);
                    server = new NettyServer(port, new NettyServerHandler.ServerConnectionCallback() {
                        ConcurrentMap<ChannelId, NetworkPlayerHelper.NetworkPlayerMessageSender> senders = new ConcurrentHashMap<>();

                        @Override
                        public void serverConnectionOpened(ChannelId id, String remoteAddress) {
                            channelIds.add(id);
                            senders.put(id, (playerMessage) -> server.sendMessage(id, playerMessage));
                            context.postEvent(new NetworkConnectionEstablishedEvent(client, remoteAddress));
                        }

                        @Override
                        public void serverConnectionMessage(ChannelId id, String sourceIpAddress, ByteBuf message) {
                            networkPlayerHelper.applyNetworkMessageToPlayer(message, id.asLongText(), context, senders.get(id), true);
                        }

                        @Override
                        public void serverConnectionClosed(ChannelId id) {
                            networkPlayerHelper.destroyNetworkPlayer(id.asLongText(), context);
                        }
                    });
                    server.listen();
                    context.getPreferences().put(Preferences.LISTENING_PORT_PREFERENCE, Integer.toString(server.getPort()));
                    String internetIpAddress = NetworkUtils.getInternetIpAddress();
                    String wsAddress = String.format("ws://%s:%d", internetIpAddress, server.getPort());
                    context.postEvent(new NetworkServerListeningEvent(wsAddress));
                    break;
                }
                case stop: {
                    server.close();
                    break;
                }
                default:
                    break;
            }
        } else if (event instanceof PlayerEvent) {
            PlayerEvent playerEvent = (PlayerEvent) event;
            if (server != null && playerEvent.getPlayerId().equalsIgnoreCase("local")) {
                String playerId = playerEvent.getPlayerId();
                double x = context.getPlayerMap().get(playerId).getX();
                double scaledBackX = context.level().get().getPixelScaleHelper().scaleXBack(x);
                switch (playerEvent.getPlayerEvent()) {
                    case moveLeft:
                        for (ChannelId id : channelIds) {
                            server.sendMessage(id, MessageBuilder.createNewMessageBuffer(MessageType.moveLeft, 3).addDoubleAsShort(scaledBackX).getBuffer());
                        }
                        break;
                    case moveRight:
                        for (ChannelId id : channelIds) {
                            server.sendMessage(id, MessageBuilder.createNewMessageBuffer(MessageType.moveRight, 3).addDoubleAsShort(scaledBackX).getBuffer());
                        }
                        break;
                    case stopMovingLeft:
                        for (ChannelId id : channelIds) {
                            server.sendMessage(id, MessageBuilder.createNewMessageBuffer(MessageType.idleLeft, 3).addDoubleAsShort(scaledBackX).getBuffer());
                        }
                        break;
                    case stopMovingRight:
                        for (ChannelId id : channelIds) {
                            server.sendMessage(id, MessageBuilder.createNewMessageBuffer(MessageType.idleRight, 3).addDoubleAsShort(scaledBackX).getBuffer());
                        }
                        break;
                    case jump:
                        for (ChannelId id : channelIds) {
                            server.sendMessage(id, MessageBuilder.createNewMessageBuffer(MessageType.jump, 3).addDoubleAsShort(scaledBackX).getBuffer());
                        }
                        break;
                }
            }
        }else if(event instanceof QuitGameEvent) {
        	if(server != null) {
        		server.close();
        	}
        }
    }

    @Override
    public Event[] getEventInterest() {
        return Event.combineEvents(PlayerEventType.values(),
                NetworkEventType.start,
                NetworkEventType.stop,
                GameEventType.quit);
    }


}
