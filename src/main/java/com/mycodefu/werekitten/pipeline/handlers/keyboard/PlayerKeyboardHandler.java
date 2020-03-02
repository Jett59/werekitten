package com.mycodefu.werekitten.pipeline.handlers.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class PlayerKeyboardHandler implements PipelineHandler {

	@Override
	public Event[] getEventInterest() {
		return new Event[] {
				GameEventType.start,
				GameEventType.quit
		};
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		
	}

}
