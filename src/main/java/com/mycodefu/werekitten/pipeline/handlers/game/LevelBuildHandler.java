package com.mycodefu.werekitten.pipeline.handlers.game;

import java.awt.Toolkit;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.BuildLevelEvent;
import com.mycodefu.werekitten.pipeline.events.game.GameEvent;
import com.mycodefu.werekitten.pipeline.events.game.LevelLoadedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class LevelBuildHandler implements PipelineHandler{

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getPipelineName().equalsIgnoreCase("pipeline") && event instanceof GameEvent) {
			GameEventType eventType = (GameEventType)event.getEvent();
			switch (eventType) {
			case buildLevel: {
			    	var screen = Toolkit.getDefaultToolkit().getScreenSize();
			        BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());
			        LevelBuilder levelBuilder = new LevelBuilder(backgroundObjectBuilder);
			        GameLevel level = levelBuilder.buildLevel("/level.wkl", screen.width, screen.height);
			        context.getStage().hide();

			        context.level().set(level);
			        context.postEvent(new LevelLoadedEvent(((BuildLevelEvent)event).shouldHost()));
				break;
			}
			
			default:
				break;
			}
		}
	}

}
