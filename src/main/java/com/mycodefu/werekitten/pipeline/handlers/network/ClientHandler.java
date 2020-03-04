package com.mycodefu.werekitten.pipeline.handlers.network;

import com.mycodefu.werekitten.event.*;
import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.NettyClientHandler;
import com.mycodefu.werekitten.network.message.MessageBuilder;
import com.mycodefu.werekitten.network.message.MessageType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.BuildLevelEvent;
import com.mycodefu.werekitten.pipeline.events.game.StartGameEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.KeyboardEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkConnectClientEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkEvent;
import com.mycodefu.werekitten.pipeline.events.player.PlayerEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent.ConnectionType;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;
import io.netty.buffer.ByteBuf;

import static com.mycodefu.werekitten.event.UiEventType.UiCreated;

public class ClientHandler implements PipelineHandler, NettyClientHandler.SocketCallback {
    private final NetworkPlayerHelper networkPlayerHelper;
    private NettyClient nettyClient;
    private PipelineContext context;

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
                    nettyClient = new NettyClient(((NetworkConnectClientEvent) event).getServerAddress(), this);
                    nettyClient.connect();
                    context.postEvent(new BuildLevelEvent(false));
                    context.postEvent(new NetworkConnectionEstablishedEvent(ConnectionType.server,
                            ((NetworkConnectClientEvent) event).getServerAddress()));
                    break;
                }

                default:
                    break;
            }
        } else if (event instanceof UiEvent) {
            switch ((UiEventType) event.getEvent()) {
                case UiCreated: {
                    if (nettyClient != null) {
                        MessageBuilder messageBuilder = MessageBuilder.createNewMessageBuffer(MessageType.init, 3)
                                .addDoubleAsShort(context.level().get().getPixelScaleHelper()
                                        .scaleXBack(context.level().get().getPlayerElement().getLocation().getX()));
                        nettyClient.sendMessage(messageBuilder.getBuffer());
                    }
                    break;
                }
                default:
                    break;
            }
        } else if (event instanceof PlayerEvent) {
            PlayerEvent playerEvent = (PlayerEvent) event;
            if (nettyClient != null && playerEvent.getPlayerId().equalsIgnoreCase("local")) {
                switch (playerEvent.getPlayerEvent()) {
                    case moveLeft:
                        nettyClient.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.moveLeft, 1).getBuffer());
                        break;
                    case moveRight:
                        nettyClient.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.moveRight, 1).getBuffer());
                        break;
                    case stopMovingLeft:
                        nettyClient.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.idleLeft, 1).getBuffer());
                        break;
                    case stopMovingRight:
                        nettyClient.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.idleRight, 1).getBuffer());
                        break;
                    case jump:
                        nettyClient.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.jump, 1).getBuffer());
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

    @Override
    public void clientConnected(String id, String remoteAddress) {

    }

    @Override
    public void clientMessageReceived(String id, ByteBuf content) {
        networkPlayerHelper.applyNetworkMessageToPlayer(content, id, context,
                message -> nettyClient.sendMessage(message), false);
    }

    @Override
    public void clientError(String id, Throwable e) {
        e.printStackTrace();
    }

    @Override
    public Event[] getEventInterest() {
        return new Event[]{
                UiCreated,
                NetworkEventType.connect,
                PlayerEventType.moveLeft,
                PlayerEventType.moveRight,
                PlayerEventType.stopMovingLeft,
                PlayerEventType.stopMovingRight,
                PlayerEventType.jump
        };
    }

}
