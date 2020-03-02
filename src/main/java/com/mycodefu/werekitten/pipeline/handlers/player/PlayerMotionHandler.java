package com.mycodefu.werekitten.pipeline.handlers.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class PlayerMotionHandler implements PipelineHandler{

	@Override
	public Event[] getEventInterest() {
		return new Event[] {
				
		};
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		// TODO Auto-generated method stub
		
	}

}
