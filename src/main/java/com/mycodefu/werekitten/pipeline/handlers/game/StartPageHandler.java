package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.GameEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.preferences.Preferences;
import com.mycodefu.werekitten.ui.StartPageUI;
import javafx.scene.Scene;

public class StartPageHandler implements PipelineHandler {
    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof GameEvent) {
            GameEvent gameEvent = (GameEvent)event;
            switch (gameEvent.getGameEvent()) {
                case start: {
                    StartPageUI startPageUI = new StartPageUI(()->context.getPreferences().get(Preferences.CLIENT_CONNECT_IP_PREFERENCE));
                    Scene scene = startPageUI.getScene(context);

                    context.getStage().setTitle("werekitten launcher");
                    context.getStage().setScene(scene);
                    context.getStage().setWidth(640);
                    context.getStage().setHeight(480);
                    context.getStage().show();
                }
			default:
				break;
            }
        }
    }

	@Override
	public Event[] getEventInterest() {
		return new Event[] {
				GameEventType.start
		};
	}
}
