package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.level.LevelBuilder;
import com.mycodefu.werekitten.level.LevelReader;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.BuildLevelEvent;
import com.mycodefu.werekitten.pipeline.events.game.GameEvent;
import com.mycodefu.werekitten.pipeline.events.game.LevelLoadedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.preferences.Preferences;

import java.awt.*;

public class LevelBuildHandler implements PipelineHandler {

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof GameEvent) {
            GameEvent gameEvent = (GameEvent)event;
            switch (gameEvent.getGameEvent()) {
                case buildLevel: {
                    var screen = Toolkit.getDefaultToolkit().getScreenSize();
                    BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());
                    LevelBuilder levelBuilder = new LevelBuilder(backgroundObjectBuilder);
                    GameLevel level = levelBuilder.buildLevel("/level.wkl", screen.width, screen.height, context.getPreferences().get(Preferences.DEFAULT_LEVEL_PREFERENCE) == null? LevelReader.defaultLevelName : context.getPreferences().get(Preferences.DEFAULT_LEVEL_PREFERENCE));
                    context.getStage().hide();

                    context.level().set(level);
                    context.postEvent(new LevelLoadedEvent(((BuildLevelEvent) event).shouldHost()));
                    break;
                }

                default:
                    break;
            }
        }
    }

    @Override
    public Event[] getEventInterest() {
        return new Event[]{
                GameEventType.buildLevel
        };
    }

}
