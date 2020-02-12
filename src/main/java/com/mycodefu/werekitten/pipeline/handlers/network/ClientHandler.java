package com.mycodefu.werekitten.pipeline.handlers.network;

import com.mycodefu.werekitten.netty.client.NettyClient;
import com.mycodefu.werekitten.netty.client.NettyClientHandler;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkConnectClientEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.NetworkPlayerHelper;


public class ClientHandler implements PipelineHandler, NettyClientHandler.SocketCallback{
	private NetworkPlayerHelper networkPlayerHelper;
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
		networkPlayerHelper.destroyNetworkPlayer(id, context);
	}
	@Override
	public void clientConnected(String id) {
		
	}
	@Override
	public void clientMessageReceived(String id, String text) {
		
	}
	@Override
	public void clientError(String id, Throwable e) {
		e.printStackTrace();
	}

}
