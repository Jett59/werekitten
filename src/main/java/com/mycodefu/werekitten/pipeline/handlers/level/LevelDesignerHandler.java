package com.mycodefu.werekitten.pipeline.handlers.level;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.level.LevelReader;
import com.mycodefu.werekitten.level.designer.LevelDesignerUI;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.LevelDesignerEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.preferences.Preferences;

public class LevelDesignerHandler implements PipelineHandler {

    @Override
    public Event[] getEventInterest() {
        return new Event[]{
                GameEventType.levelDesigner
        };
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof LevelDesignerEvent) {
            LevelDesignerUI ui = new LevelDesignerUI();
            context.getStage().hide();
            context.getStage().setScene(ui.getScene(context.getPreferences().getOrDefault(Preferences.DEFAULT_LEVEL_PREFERENCE, LevelReader.defaultLevelName)));
            context.getStage().setWidth(LevelDesignerUI.LEVEL_WIDTH + LevelDesignerUI.TOOL_WIDTH);
            context.getStage().setHeight(LevelDesignerUI.LEVEL_HEIGHT);
            context.getStage().show();
        }
    }

}
