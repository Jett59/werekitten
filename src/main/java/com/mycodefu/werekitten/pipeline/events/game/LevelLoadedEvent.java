package com.mycodefu.werekitten.pipeline.events.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;

public class LevelLoadedEvent extends GameEvent {
	private boolean shouldListenOnLan;
	
    @Override
    public Event getEvent() {
        return GameEventType.levelLoaded;
    }
    
    public LevelLoadedEvent(boolean shouldListenOnLan) {
    	this.shouldListenOnLan = shouldListenOnLan;
    }
    
    public boolean getShouldListenOnLan() {
    	return shouldListenOnLan;
    }
}
