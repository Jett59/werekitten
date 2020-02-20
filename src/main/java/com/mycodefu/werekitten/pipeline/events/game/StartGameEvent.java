package com.mycodefu.werekitten.pipeline.events.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;

public class StartGameEvent extends GameEvent {
	private String name = "start";
	
    @Override
    public Event getEvent() {
        return GameEventType.start;
    }
}
