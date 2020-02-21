package com.mycodefu.werekitten.pipeline.handlers.network;

import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.NettyClientHandler;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.BuildLevelEvent;
import com.mycodefu.werekitten.pipeline.events.game.StartGameEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkConnectClientEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent.ConnectionType;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;

import static com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent.ConnectionType.client;


public class ClientHandler implements PipelineHandler, NettyClientHandler.SocketCallback{
	private final NetworkPlayerHelper networkPlayerHelper;
	private NettyClient nettyClient;
	private PipelineContext context;

public ClientHandler() {
	this.networkPlayerHelper = new NetworkPlayerHelper();
}
	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getPipelineName().equalsIgnoreCase("network")) {
			this.context = context;
			NetworkEvent networkEvent = (NetworkEvent)event;
			switch (networkEvent.getNetworkEvent()) {
			case connect: {
				System.out.println("connect event recieved "+event.toString());
				nettyClient = new NettyClient(((NetworkConnectClientEvent)event).getServerAddress(), this);
				nettyClient.connect();
				context.postEvent(new BuildLevelEvent(false));
				context.postEvent(new NetworkConnectionEstablishedEvent(ConnectionType.server, ((NetworkConnectClientEvent)event).getServerAddress()));
				break;
			}
			case readyForInitMessage: {
				nettyClient.sendMessage("init"+(int)context.level().get().getPixelScaleHelper().scaleXBack(context.level().get().getPlayerElement().getLocation().getX()));
				break;
			}
			
			default:
				break;
			}
		}else {
			 throw new IllegalArgumentException("the handler ClientHandler is not allow on the pipeline "+event.getPipelineName()+", should only be on network");
		}
	}
	@Override
	public void clientDisconnected(String id) {
		System.out.println("client destroying player");
		networkPlayerHelper.destroyNetworkPlayer(id, context);
		context.postEvent(new StartGameEvent());
	}

	@Override
	public void clientConnected(String id, String remoteAddress) {
			
	}

	@Override
	public void clientMessageReceived(String id, String text) {
		networkPlayerHelper.applyNetworkMessageToPlayer(text, id, context, message -> nettyClient.sendMessage(message), false);
	}
	@Override
	public void clientError(String id, Throwable e) {
	e.printStackTrace();
	}

}
