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

				var screenSize = Toolkit.getDefaultToolkit().getScreenSize();

				context.getStage().setHeight(screenSize.height);
				context.getStage().setWidth(screenSize.width);

				context.getStage().setTitle("werekitten");
				context.getStage().setMaximized(true);
				context.getStage().show();
				ui = new GameUI();
				context.getStage().setScene(
						ui
								.getScene((int)context.getStage().getWidth(), (int)context.getStage().getHeight())
				);
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
