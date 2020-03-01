package com.mycodefu.werekitten.pipeline.handlers.Ui;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerCreatedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerDestroyedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.ui.UI;

public class PlayerAdditionHandler implements PipelineHandler{
private UI ui;

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getPipelineName().equalsIgnoreCase("pipeline")) {
			switch (event.getEvent().getName()) {
			case "UiCreated": {
				System.out.println("UI created");
				this.ui = ((UiCreatedEvent)event).getUI();
				break;
			}
			case "playerCreated": {
				ui.addPlayer(((PlayerCreatedEvent)event).getPlayer());
				break;
			}
			case "playerDestroyed": {
				ui.removePlayer(((PlayerDestroyedEvent)event).getPlayer());
				break;
			}
			
			default:
				break;
			}
		}
	}

}
