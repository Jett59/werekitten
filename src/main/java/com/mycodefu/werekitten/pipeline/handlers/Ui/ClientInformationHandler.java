package com.mycodefu.werekitten.pipeline.handlers.Ui;

import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;
import com.mycodefu.werekitten.pipeline.events.network.ReadyForNetworkInitMessageEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectionEstablishedEvent.ConnectionType;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class ClientInformationHandler implements PipelineHandler{
	private ConnectionType connectionType = null;

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getPipelineName().equalsIgnoreCase("ui")) {
			UiEventType eventType = (UiEventType)((UiEvent)event).getEvent();
			switch (eventType) {
			case networkConnectionEstablished: {
				connectionType = ((NetworkConnectionEstablishedEvent)event).getConnectionType();
				break;
			}
			case UiCreated: {
				if(connectionType == null) {
					return;
				}
				if(connectionType.equals(ConnectionType.server)) {
					context.postEvent(new ReadyForNetworkInitMessageEvent());
				}
			}
			
			default:
				break;
			}
		}else {
			throw new IllegalArgumentException("the handler ClientInformationHandler is only allowed in the ui pipeline, not in "+event.getPipelineName());
		}
	}

}
