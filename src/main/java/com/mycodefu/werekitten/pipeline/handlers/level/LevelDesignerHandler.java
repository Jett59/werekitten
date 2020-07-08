package com.mycodefu.werekitten.pipeline.handlers.level;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.level.designer.LevelDesignerUI;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.LevelDesignerEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class LevelDesignerHandler implements PipelineHandler{

	@Override
	public Event[] getEventInterest() {
		return new Event[] {
				GameEventType.levelDesigner
		};
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event instanceof LevelDesignerEvent) {
			LevelDesignerUI ui = new LevelDesignerUI();
			context.getStage().hide();
			context.getStage().setScene(ui.getScene());
			context.getStage().setWidth(LevelDesignerUI.LEVEL_WIDTH+250);
			context.getStage().setHeight(LevelDesignerUI.LEVEL_HEIGHT);
			context.getStage().show();
		}
	}

}
