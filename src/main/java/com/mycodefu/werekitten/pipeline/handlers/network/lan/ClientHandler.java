package com.mycodefu.werekitten.pipeline.handlers.network.lan;

import com.mycodefu.werekitten.event.*;
import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.SocketCallback;
import com.mycodefu.werekitten.network.message.*;
import com.mycodefu.werekitten.network.message.ServerMessage.IntroductionType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.chat.ChatMessageSendEvent;
import com.mycodefu.werekitten.pipeline.events.chat.ChatMessageSentEvent;
import com.mycodefu.werekitten.pipeline.events.game.BuildLevelEvent;
import com.mycodefu.werekitten.pipeline.events.game.StartGameEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkConnectClientEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkEvent;
import com.mycodefu.werekitten.pipeline.events.player.PlayerEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent.ConnectionType;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;
import com.mycodefu.werekitten.preferences.Preferences;

import static com.mycodefu.werekitten.event.UiEventType.UiCreated;

public class ClientHandler implements PipelineHandler, SocketCallback {
    private final NetworkPlayerHelper networkPlayerHelper;
    private NettyClient nettyClient;
    private PipelineContext context;
    private String serverAddress;

    public ClientHandler() {
        this.networkPlayerHelper = new NetworkPlayerHelper();
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof NetworkEvent) {
            this.context = context;
            NetworkEvent networkEvent = (NetworkEvent) event;
            switch (networkEvent.getNetworkEvent()) {
                case connect: {
                    serverAddress = ((NetworkConnectClientEvent) event).getServerAddress();

                    nettyClient = new NettyClient("ws://werekitten.mycodefu.com:51273", this);
                    nettyClient.connect();
                    context.postEvent(new BuildLevelEvent(false));
                    context.postEvent(new NetworkConnectionEstablishedEvent(ConnectionType.server, serverAddress));
                    break;
                }

                default:
                    break;
            }
        } else if (event instanceof UiEvent) {
            switch ((UiEventType) event.getEvent()) {
                case UiCreated: {
                    if (nettyClient != null) {
                        double x = context.level().get().getPixelScaleHelper().scaleXBack(context.level().get().getPlayerElement().getLocation().getX());
                        nettyClient.sendMessage(new XSyncMessage(MessageType.init, x));
                    }
                    break;
                }
                default:
                    break;
            }
        } else if (event instanceof ChatMessageSendEvent) {
            if (nettyClient != null) {
                ChatMessageSendEvent sendEvent = (ChatMessageSendEvent) event;
                nettyClient.sendMessage(new ChatMessage(System.currentTimeMillis(), sendEvent.message));
                context.postEvent(new ChatMessageSentEvent());
                System.out.println("posted sent event");
            }
        } else if (event instanceof PlayerEvent) {
            PlayerEvent playerEvent = (PlayerEvent) event;
            if (nettyClient != null && playerEvent.getPlayerId().equalsIgnoreCase("local")) {
                String playerId = playerEvent.getPlayerId();
                double x = context.getPlayerMap().get(playerId).getX();
                double scaledBackX = context.level().get().getPixelScaleHelper().scaleXBack(x);
                switch (playerEvent.getPlayerEvent()) {
                    case moveLeft:
                        nettyClient.sendMessage(new XSyncMessage(MessageType.moveLeft, scaledBackX));
                        break;
                    case moveRight:
                        nettyClient.sendMessage(new XSyncMessage(MessageType.moveRight, scaledBackX));
                        break;
                    case stopMovingLeft:
                        nettyClient.sendMessage(new XSyncMessage(MessageType.idleLeft, scaledBackX));
                        break;
                    case stopMovingRight:
                        nettyClient.sendMessage(new XSyncMessage(MessageType.idleRight, scaledBackX));
                        break;
                    case jump:
                        nettyClient.sendMessage(new XSyncMessage(MessageType.jump, scaledBackX));
                        break;
                }
            }
        }
    }

    @Override
    public void clientDisconnected(String id) {
        networkPlayerHelper.destroyNetworkPlayer(id, context);
        context.postEvent(new StartGameEvent());
    }

    int pongs = 0;
    long start = 0;
    long finished = 0;
    long time = 0;

    @Override
    public void clientConnected(String id, String remoteAddress) {
        nettyClient.sendMessage(ServerMessage.introductionMessage(Integer.parseInt(serverAddress), IntroductionType.JOIN));
        context.getPreferences().put(Preferences.CLIENT_CONNECT_IP_PREFERENCE, serverAddress);
        nettyClient.sendMessage(new Message(MessageType.ping));
        start = System.nanoTime();
    }

    @Override
    public void clientMessageReceived(String id, Message message) {
        if (message.type == MessageType.pong) {
            finished = System.nanoTime();
            pongs++;
            time += finished - start;
            if (pongs < 10) {
                nettyClient.sendMessage(new Message(MessageType.ping));
                start = System.nanoTime();
            } else {
                time /= pongs;
                time /= 1000000;
                time /= 2;
                System.out.println("average latency: " + time);
                nettyClient.sendMessage(new PangMessage(time));
            }
        }
        networkPlayerHelper.applyNetworkMessageToPlayer(message, id, context,
                msg -> nettyClient.sendMessage(msg), false);
    }

    @Override
    public void clientError(String id, Throwable e) {
        e.printStackTrace();
    }

    @Override
    public Event[] getEventInterest() {
        return Event.combineEvents(PlayerEventType.values(),
            ChatEventType.send,
            UiCreated
        ); //TODO: Create a new variation of NetworkEventType.connect for LAN clients
    }

}
