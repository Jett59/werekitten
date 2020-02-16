package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.GameEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.RegisterKeyListenerEvent;
import com.mycodefu.werekitten.pipeline.handlers.*;

public class OnFullScreenExitRestartHandler implements PipelineHandler, RegisterKeyListenerEvent.KeyListener {

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getPipelineName().equalsIgnoreCase("game")) {
			switch ((GameEventType)event.getEvent()) {
			case start: {
				
			}
			
			default:
				break;
			}
		}else {
			throw new IllegalArgumentException("the OnFullScreenExitRestartHandler is only allowed in the game pipeline, not the pipeline: "+event.getPipelineName());
		}
	}

	@Override
	public void keyEventOccurred(KeyboardEventType keyboardEventType) {
		
	}

}
