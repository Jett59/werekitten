package com.mycodefu.werekitten.pipeline.handlers.Ui;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.ui.UI;

public class LocalPlayerAdditionHandler implements PipelineHandler{
private UI ui;

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getPipelineName().equalsIgnoreCase("Ui")) {
			switch (event.getEvent().getName()) {
			case "UiCreated": {
				this.ui = ((UiCreatedEvent)event).getUI();
				break;
			}
			case "playerCreated": {
				//ui.
			}
			
			default:
				break;
			}
		}
	}

}
