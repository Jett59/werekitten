package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.GameEvent;
import com.mycodefu.werekitten.pipeline.events.game.QuitGameEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class WindowCloseHandler implements PipelineHandler {

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof GameEvent) {
            GameEvent gameEvent = (GameEvent)event;
            switch (gameEvent.getGameEvent()) {
                case start: {
                    context.getStage().setOnCloseRequest(e -> {
                        context.postEvent(new QuitGameEvent());
                    });
                    break;
                }
                case quit: {
                    System.exit(0);
                }
			default:
				break;
            }
        }
    }

	@Override
	public Event[] getEventInterest() {
		return new Event[]{
				GameEventType.start,
				GameEventType.quit
		};
	}

}
