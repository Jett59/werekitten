package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.GameEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.sound.MusicPlayer;

public class MusicHandler implements PipelineHandler {

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof GameEvent) {
            GameEvent gameEvent = (GameEvent)event;
            switch (gameEvent.getGameEvent()) {
                case start: {
                	MusicPlayer.stop();
                	break;
                }
				case quit: {
                    MusicPlayer.stop();
                    MusicPlayer.shutdown();
                    break;
                }

                case levelLoaded: {
                    MusicPlayer.play();
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
                GameEventType.start,
                GameEventType.levelLoaded,
                GameEventType.quit
        };
    }

}
