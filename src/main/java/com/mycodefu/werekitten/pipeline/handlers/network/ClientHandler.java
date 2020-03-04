package com.mycodefu.werekitten.pipeline.handlers.network;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.event.NetworkEventType;
import com.mycodefu.werekitten.event.UiEventType;
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
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent.ConnectionType;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;
import io.netty.buffer.ByteBuf;

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
        return new Event[] {UiEventType.UiCreated, NetworkEventType.connect};
    }

}
