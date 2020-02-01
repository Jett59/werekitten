package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.ui.GameUI;

import java.awt.*;

public class UICreationHandler implements PipelineHandler {
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
    	if(event.getPipelineName().equalsIgnoreCase("game")) {
    		switch (event.getEvent().getName()) {
    		case "start": {
    			GameUI ui;

				context.getStage().setTitle("werekitten");

				var screenSize = Toolkit.getDefaultToolkit().getScreenSize();

				ui = new GameUI();
    			context.getStage().setScene(ui.getScene(screenSize.width, screenSize.height));
				context.getStage().setFullScreen(true);
				context.getStage().show();

    			context.postEvent(new UiCreatedEvent(ui));
    			break;
    		}
    		
    		case "quit": {
    			context.getStage().hide();
    			break;
    		}
    		
    		default:
    			break;
    		}
    	}
    }
}
