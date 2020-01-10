package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.ui.GameUI;

public class UICreationHandler implements PipelineHandler {
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
    	if(event.getPipelineName().equalsIgnoreCase("game")) {
    		switch (event.getEvent().getName()) {
    		case "start": {
    			context.getStage().show();
    			context.getStage().setScene(
    					new GameUI()
    					.getScene(1024, 768)
    					);
    			break;
    		}
    		
    		case "quit": {
    			context.getStage().hide();
    			break;
    		}
    		
    		default:
    			throw new IllegalArgumentException("gameEventType "+event.getEvent().getName()+" could not be determined");
    		}
    	}
    }
}
