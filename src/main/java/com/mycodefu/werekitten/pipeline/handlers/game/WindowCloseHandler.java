package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.PipelineEventType;
import com.mycodefu.werekitten.pipeline.events.game.QuitGameEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class WindowCloseHandler implements PipelineHandler{

	@Override
	public PipelineEventType[] registerEventTypeInterest() {
		return new PipelineEventType[] {PipelineEventType.Game};
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getEventType().name().equalsIgnoreCase("game")) {
			switch (event.getEvent().getName()) {
			case "start": {
				context.getStage().setOnCloseRequest(e->{
					context.postEvent(new QuitGameEvent());
				});
				break;
			}
			case "quit": {
				System.exit(0);
			}
			default:
				throw new IllegalArgumentException("gameEventType: "+event.getEvent().getName()+" could not be determined");
			}
		}else {
			throw new IllegalArgumentException("the eventType "+event.getEventType().name()+" is not allowed in the handler windowCloseHandler");
		}
	}

}
