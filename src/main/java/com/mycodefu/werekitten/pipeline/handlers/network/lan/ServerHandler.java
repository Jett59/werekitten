package com.mycodefu.werekitten.pipeline.handlers.network.lan;

import com.mycodefu.werekitten.event.*;
import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.SocketCallback;
import com.mycodefu.werekitten.network.message.*;
import com.mycodefu.werekitten.network.message.ServerMessage.IntroductionType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.chat.ChatMessageSendEvent;
import com.mycodefu.werekitten.pipeline.events.game.QuitGameEvent;
import com.mycodefu.werekitten.pipeline.events.game.StartGameEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkEvent;
import com.mycodefu.werekitten.pipeline.events.player.PlayerEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkServerListeningEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;
import com.mycodefu.werekitten.preferences.Preferences;

public class ServerHandler implements PipelineHandler {
    private final NetworkPlayerHelper networkPlayerHelper;
    private NettyClient server;

    public ServerHandler() {
        this.networkPlayerHelper = new NetworkPlayerHelper();
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof NetworkEvent) {
            NetworkEvent networkEvent = (NetworkEvent) event;
            switch (networkEvent.getNetworkEvent()) {
                case start: {
                    String portPreference = context.getPreferences().get(Preferences.LISTENING_PORT_PREFERENCE);
                    int port = portPreference == null || portPreference == "" ? context.getListeningPort() : Integer.parseInt(portPreference);
                    server = new NettyClient("ws://werekitten.mycodefu.com:51273", (SocketCallback) new SocketCallback() {

                        @Override
                        public void clientMessageReceived(String id, Message content) {
                            // TODO Auto-generated method stub
                            if (content.type == MessageType.ping) {
                                server.sendMessage(new Message(MessageType.pong));
                            }
                            networkPlayerHelper.applyNetworkMessageToPlayer(content, id, context,
                                    message -> server.sendMessage(message), true);
                        }

                        @Override
                        public void clientError(String id, Throwable e) {
                            // TODO Auto-generated method stub
                            e.printStackTrace();
                        }

                        @Override
                        public void clientDisconnected(String id) {
                            // TODO Auto-generated method stub
                            networkPlayerHelper.destroyNetworkPlayer(id, context);
                            context.postEvent(new StartGameEvent());
                        }

                        @Override
                        public void clientConnected(String id, String remoteAddress) {
                            server.sendMessage(ServerMessage.introductionMessage(port, IntroductionType.HOST));
                        }
                    });
                    server.connect();
                    String address = Integer.toString(port);
                    context.postEvent(new NetworkServerListeningEvent(address));
                    break;
                }
                case stop: {
                    server.disconnect();
                    break;
                }
                default:
                    break;
            }
        } else if (event instanceof ChatMessageSendEvent) {
            if (server != null) {
                String message = ((ChatMessageSendEvent) event).message;
                server.sendMessage(new ChatMessage(message));
            }
        } else if (event instanceof PlayerEvent) {
            PlayerEvent playerEvent = (PlayerEvent) event;
            if (server != null && playerEvent.getPlayerId().equalsIgnoreCase("local")) {
                String playerId = playerEvent.getPlayerId();
                double x = context.getPlayerMap().get(playerId).getX();
                double scaledBackX = context.level().get().getPixelScaleHelper().scaleXBack(x);
                switch (playerEvent.getPlayerEvent()) {
                    case moveLeft:
                        server.sendMessage(new XSyncMessage(MessageType.moveLeft, scaledBackX));
                        break;
                    case moveRight:
                        server.sendMessage(new XSyncMessage(MessageType.moveRight, scaledBackX));
                        break;
                    case stopMovingLeft:
                        server.sendMessage(new XSyncMessage(MessageType.idleLeft, scaledBackX));
                        break;
                    case stopMovingRight:
                        server.sendMessage(new XSyncMessage(MessageType.idleRight, scaledBackX));
                        break;
                    case jump:
                        server.sendMessage(new XSyncMessage(MessageType.jump, scaledBackX));
                        break;
                }
            }
        } else if (event instanceof QuitGameEvent) {
            if (server != null) {
                server.disconnect();
            }
        }
    }

    @Override
    public Event[] getEventInterest() {
        return Event.combineEvents(PlayerEventType.values(), new Event[]{
                NetworkEventType.start,
                NetworkEventType.stop,
                GameEventType.quit,
                ChatEventType.send});
    }


}
