package com.mycodefu.werekitten.pipeline.handlers.network;

import com.mycodefu.werekitten.event.ChatEventType;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.event.NetworkEventType;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.SocketCallback;
import com.mycodefu.werekitten.network.message.Message;
import com.mycodefu.werekitten.network.message.MessageBuilder;
import com.mycodefu.werekitten.network.message.MessageType;
import com.mycodefu.werekitten.network.message.ServerMessage;
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

import io.netty.util.CharsetUtil;

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
							if(content.type == MessageType.ping) {
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
        }else if(event instanceof ChatMessageSendEvent) {
        	if(server != null) {
        		String message = ((ChatMessageSendEvent)event).message;
        		MessageBuilder messageBuilder = MessageBuilder.createNewMessageBuffer(MessageType.chat, message.length()+1).putByte((byte)message.length()).putBytes(message.getBytes(CharsetUtil.UTF_8));
        			server.sendMessage(messageBuilder.getBuffer());
        	}
        } else if (event instanceof PlayerEvent) {
            PlayerEvent playerEvent = (PlayerEvent) event;
            if (server != null && playerEvent.getPlayerId().equalsIgnoreCase("local")) {
                String playerId = playerEvent.getPlayerId();
                double x = context.getPlayerMap().get(playerId).getX();
                double scaledBackX = context.level().get().getPixelScaleHelper().scaleXBack(x);
                switch (playerEvent.getPlayerEvent()) {
                    case moveLeft:
                            server.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.moveLeft, 2).addDoubleAsShort(scaledBackX).getBuffer());
                        break;
                    case moveRight:
                            server.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.moveRight, 2).addDoubleAsShort(scaledBackX).getBuffer());
                        break;
                    case stopMovingLeft:
                            server.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.idleLeft, 2).addDoubleAsShort(scaledBackX).getBuffer());
                        break;
                    case stopMovingRight:
                            server.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.idleRight, 2).addDoubleAsShort(scaledBackX).getBuffer());
                        break;
                    case jump:
                            server.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.jump, 2).addDoubleAsShort(scaledBackX).getBuffer());
                        break;
                }
            }
        }else if(event instanceof QuitGameEvent) {
        	if(server != null) {
        		server.disconnect();
        	}
        }
        }
    
    @Override
    public Event[] getEventInterest() {
        return Event.combineEvents(PlayerEventType.values(), new Event[] {
                NetworkEventType.start,
                NetworkEventType.stop,
                GameEventType.quit,
                ChatEventType.send});
    }


}
