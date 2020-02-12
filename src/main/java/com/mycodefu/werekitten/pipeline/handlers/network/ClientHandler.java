package com.mycodefu.werekitten.pipeline.handlers.network;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class ClientHandler implements PipelineHandler{

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getPipelineName().equalsIgnoreCase("network")) {
			NetworkEvent networkEvent = (NetworkEvent)event;
			switch (networkEvent.getNetworkEvent()) {
			case connect: {
				System.out.println("connect event recieved "+event.toString());
				break;
			}
			
			default:
				break;
			}
		}else {
			 throw new IllegalArgumentException("the handler ClientHandler is not allow on the pipeline "+event.getPipelineName()+", should only be on network");
		}
	}

}
