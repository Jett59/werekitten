package com.mycodefu.werekitten.pipeline.handlers.Ui;

import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.NettyClientHandler;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkStartEvent;
import com.mycodefu.werekitten.pipeline.events.ui.*;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Kitten;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.position.PixelScaleHelper;
import com.mycodefu.werekitten.ui.UI;
import com.mycodefu.werekitten.ui.UIEventCallback;

public class UiNetworkEventHandler implements PipelineHandler, UIEventCallback, NettyClientHandler.SocketCallback {
	private final NetworkPlayerHelper networkPlayerHelper;
	private UI ui;
    private NettyClient nettyClient;
	private PipelineContext context;

	public UiNetworkEventHandler(){
		this.networkPlayerHelper = new NetworkPlayerHelper();
	}

	@Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
    	this.context = context;
        if (event.getPipelineName().equalsIgnoreCase("Ui")) {
			PixelScaleHelper pixelScaleHelper = context.level().get().getPixelScaleHelper();
			switch ((UiEventType)event.getEvent()) {
                case UiCreated: {
                    this.ui = ((UiCreatedEvent) event).getUI();
                    this.ui.addUIEventListener(this);
					context.postEvent(new NetworkStartEvent());
                    break;
                }
				case networkMoveLeft:{
					Player player = context.getPlayerMap().get(((NetworkMoveLeftEvent)event).getPlayerId());
					player.moveLeft(pixelScaleHelper.scaleX(Kitten.MOVE_AMOUNT));
					break;
				}
				case networkMoveRight:{
					Player player = context.getPlayerMap().get(((NetworkMoveRightEvent)event).getPlayerId());
					player.moveRight(pixelScaleHelper.scaleX(Kitten.MOVE_AMOUNT));
					break;
				}
				case networkStopMovingLeft:{
					Player player = context.getPlayerMap().get(((NetworkStopMovingLeftEvent)event).getPlayerId());
					player.stopMovingLeft();
					break;
				}
				case networkStopMovingRight:{
					Player player = context.getPlayerMap().get(((NetworkStopMovingRightEvent)event).getPlayerId());
					player.stopMovingRight();
					break;
				}
				case networkJump:{
					Player player = context.getPlayerMap().get(((NetworkJumpEvent)event).getPlayerId());
					player.jump();
					break;
				}
				case networkConnected: {
                    ui.updateConnectionState(true);
                    break;
                }
				case networkDisconnected: {
                    ui.updateConnectionState(false);
                    break;
                }
				case networkServerListening: {
                    ui.setPort(((NetworkServerListeningEvent)event).getPort());
                    break;
                }

                default:
                    break;
            }
        }
    }

	@Override
	public void connect(String address) {
		nettyClient = new NettyClient(address, this);
		nettyClient.connect();
	}

	@Override
	public void disconnect() {
		if (nettyClient != null) {
			nettyClient.disconnect();
		}
	}

	@Override
	public void clientDisconnected(String id) {
		networkPlayerHelper.destroyNetworkPlayer(id, context);
	}

	@Override
	public void clientConnected(String id) {
		networkPlayerHelper.createNetworkPlayer(id, context, (message) -> {
			if (nettyClient != null){
				nettyClient.sendMessage(message);
			}
		});
	}

	@Override
	public void clientMessageReceived(String id, String text) {
		networkPlayerHelper.applyNetworkMessageToPlayer(text, id, context);
	}

	@Override
	public void clientError(String id, Throwable e) {
		e.printStackTrace();
	}
}
