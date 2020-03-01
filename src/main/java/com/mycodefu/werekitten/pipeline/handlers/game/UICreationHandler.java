package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.GameEvent;
import com.mycodefu.werekitten.pipeline.events.game.LevelLoadedEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.KeyboardEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkStartEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.slide.LayerGroup;
import com.mycodefu.werekitten.ui.GameUI;
import com.mycodefu.werekitten.ui.SceneLevel;

import java.awt.*;
import java.util.List;

public class UICreationHandler implements PipelineHandler {
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
    	if(event.getPipelineName().equalsIgnoreCase("pipeline") && event instanceof GameEvent) {
			GameEventType gameEventType = (GameEventType)event.getEvent();
    		switch (gameEventType) {
    		case levelLoaded: {
				GameUI ui;

				context.getStage().setTitle("werekitten");

				ui = new GameUI();
				SceneLevel sceneLevel = ui.getScene(context.level().get());
				context.level().set(sceneLevel.getLevel());
				System.out.println("level built");
				context.getStage().setScene(sceneLevel.getScene());
				context.getStage().setFullScreen(true);
				context.getStage().show();

    			context.postEvent(new UiCreatedEvent(ui));
    			
    			boolean shouldListenOnLan = ((LevelLoadedEvent)event).getShouldListenOnLan();
    			if(shouldListenOnLan) {
    				context.postEvent(new NetworkStartEvent());
    			}
    			break;
    		}
    		
    		case quit: {
    			context.getStage().hide();
    			break;
    		}
    		
    		default:
    			break;
    		}
    	}
    }

	@Override
	public Event[] getEventInterest() {
		return new Event[] {
				GameEventType.levelLoaded,
				GameEventType.quit
		};
	}
}
